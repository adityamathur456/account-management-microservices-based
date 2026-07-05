package com.transaction.service;

import com.transaction.enums.AccountStatus;
import com.transaction.client.AccountClient;
import com.transaction.dto.AccountDTO;
import com.transaction.dto.TransactionRequestDTO;
import com.transaction.dto.TransactionResponseDTO;
import com.transaction.entity.Transaction;
import com.transaction.enums.TransactionStatus;
import com.transaction.enums.TransactionType;
import com.transaction.mapper.TransactionMapper;
import com.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;
    private final TransactionMapper mapper;

    @Override
    public TransactionResponseDTO createTransaction(TransactionRequestDTO request) {
        Transaction transaction = mapper.toEntity(request);
        transaction.setTransactionId(UUID.randomUUID().toString().substring(0, 12));
        transaction.setStatus(TransactionStatus.PENDING);

        try {
            AccountDTO source = accountClient.getAccountByAccountNo(request.getSourceAccountNo());
            AccountDTO dest = accountClient.getAccountByAccountNo(request.getDestinationAccountNo());

            if (source.getStatus() != AccountStatus.ACTIVE || dest.getStatus() != AccountStatus.ACTIVE) {
                transaction.setStatus(TransactionStatus.FAILED);
                transaction.setRemarks("Account not ACTIVE");
                return mapper.toResponseDTO(transactionRepository.save(transaction));
            }

            if (request.getAmount() > source.getBalance()) {
                transaction.setStatus(TransactionStatus.FAILED);
                transaction.setRemarks("Insufficient balance");
                return mapper.toResponseDTO(transactionRepository.save(transaction));
            }

            accountClient.debit(source.getAccountNo(), request.getAmount());
            accountClient.credit(dest.getAccountNo(), request.getAmount());

            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setTransactionType(TransactionType.TRANSFER);
            transaction.setRemarks("Transaction successful: debited from " + source.getAccountNo()
                    + " and credited to " + dest.getAccountNo());
        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setRemarks("Transaction failed " + e.getMessage());
        }

        return mapper.toResponseDTO(transactionRepository.save(transaction));
    }

    @Override
    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream().map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionResponseDTO getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(mapper::toResponseDTO)
                .orElse(null);
    }

    @Override
    public List<Transaction> getTransactionByTransactionId(String txnId) {
        return List.of();
    }

    @Override
    public List<TransactionResponseDTO> getTransactionsByAccount(String accountNo) {
        return transactionRepository.findBySourceAccountNo(accountNo)
                .stream().map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
