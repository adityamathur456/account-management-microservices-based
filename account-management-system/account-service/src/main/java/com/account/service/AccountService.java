package com.account.service;



import com.account.dto.AccountDTO;
import com.account.dto.AccountDetailsDTO;
import com.account.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    AccountDTO createAccount(AccountDTO dto);

    AccountDetailsDTO getAccountDetails(String accountNo);


    Optional<AccountDTO> getAccountByCustomerId(String customerId);

    List<AccountDTO> getAllAccounts();

    boolean deleteAccount(String accountNo);

//    AccountDTO updateBalance(String accountNo, Double amount);

    AccountDTO getByAccountNo(String accountNo);

    void debit(String accountNo, Double amount);

    void credit(String accountNo, Double amount);
}
