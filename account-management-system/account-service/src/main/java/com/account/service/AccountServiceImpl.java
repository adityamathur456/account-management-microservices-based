package com.account.service;

import com.account.client.CustomerClient;
import com.account.dto.AccountDTO;
import com.account.dto.AccountDetailsDTO;
import com.account.dto.CustomerDTO;
import com.account.entity.Account;
import com.account.enums.AccountStatus;
import com.account.exception.CustomerAlreadyHasAccountException;
import com.account.exception.CustomerIdRequiredException;
import com.account.exception.CustomerNotFoundException;
import com.account.mapper.AccountMapper;
import com.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CustomerClient customerClient; // ✅ Inject Feign client

    private static final SecureRandom random = new SecureRandom();

    // Generate unique 12-digit account number
    private String generateAccountNo() {
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10)); // 0-9 digit
        }
        return sb.toString();
    }

    // Check customer already have account if account present return true otherwise false
    public boolean customerAccountIsPresent(String customerId) {
        Optional<Account> account = accountRepository.findByCustomerId(customerId);
        return account.isPresent();
    }

    @Override
    public Optional<AccountDTO> getAccountByCustomerId(String customerId) {
        return accountRepository.findByCustomerId(customerId).map(accountMapper::toDto);
    }

    @Override
    public AccountDTO createAccount(AccountDTO dto) {
        // ✅ 1. Validate CustomerId
        if (dto.getCustomerId() == null || dto.getCustomerId().isBlank())
            throw new CustomerIdRequiredException();

        if(customerAccountIsPresent(dto.getCustomerId()))
            throw new CustomerAlreadyHasAccountException(dto.getCustomerId());

        boolean exists = customerClient.checkCustomerExists(dto.getCustomerId());
        if (!exists) {
            throw new CustomerNotFoundException(dto.getCustomerId());
        }

        // ✅ 2. Validate Account Type
        if (dto.getAccountType() == null) {
            throw new IllegalArgumentException("Account type is required (e.g., SAVINGS, CURRENT).");
        }

        // ✅ 3. Map DTO → Entity
        Account account = accountMapper.toEntity(dto);
        account.setAccountNo(generateAccountNo());

        // ✅ 4. Default status = ACTIVE if not provided
        if (account.getStatus() == null) {
            account.setStatus(AccountStatus.ACTIVE);
        }

        // ✅ 5. Default balance = 0.0 if not provided
        if (account.getBalance() == null) {
            account.setBalance(0.0);
        }

        // ✅ 6. Save
        account = accountRepository.save(account);
        return accountMapper.toDto(account);
    }

    @Override
    public AccountDTO getByAccountNo(String accountNo) {
        Optional<Account> account = accountRepository.findByAccountNo(accountNo);
        return account.map(accountMapper::toDto).orElse(null);
    }

    @Override
    @Transactional
    public void debit(String accountNo, Double amount) {
        Account account = accountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void credit(String accountNo, Double amount) {
        Account account = accountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

//    @Override
//    @Transactional
//    public AccountDTO updateBalance(String accountNo, Double amount) {
//        if (amount == null) {
//            throw new IllegalArgumentException("Amount is required.");
//        }
//
//        // ✅ Fetch account
//        Account account = accountRepository.findByAccountNo(accountNo)
//                .orElseThrow(() -> new RuntimeException("Account not found"));
//
//        // ✅ Check status
//        if (account.getStatus() != AccountStatus.ACTIVE) {
//            throw new IllegalStateException("Cannot update balance. Account is not ACTIVE.");
//        }
//
//        // ✅ Calculate new balance
//        double newBalance = account.getBalance() + amount;
//
//        // ✅ Prevent negative balance
//        if (newBalance < 0) {
//            throw new IllegalArgumentException("Insufficient balance. Cannot go below 0.");
//        }
//
//        // ✅ Update balance
//        account.setBalance(newBalance);
//
//        // ✅ Save & return updated DTO
//        account = accountRepository.save(account);
//        return accountMapper.toDto(account);
//    }

    public AccountDetailsDTO getAccountDetails(String accountNo) {
        Account account = accountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Call customer-service
        CustomerDTO customer = customerClient.getCustomerById(account.getCustomerId());

        return AccountDetailsDTO.builder()
                .accountNo(account.getAccountNo())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .status(account.getStatus())
                .customerName(customer.getFirstName() +" "+ customer.getLastName())
                .customerEmail(customer.getEmail())
                .customerDateOfBirth(customer.getDateOfBirth())
                .customerAddress(customer.getAddress())
                .build();
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll()
                .stream().map(accountMapper::toDto).toList();
    }

    @Override
    @Transactional
    public boolean deleteAccount(String accountNo) {
        return accountRepository.deleteByAccountNo(accountNo) > 0.0;
    }
}
