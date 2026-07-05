package com.transaction.service;

import com.transaction.dto.TransactionRequestDTO;
import com.transaction.dto.TransactionResponseDTO;
import com.transaction.entity.Transaction;

import java.util.List;

public interface TransactionService {
    TransactionResponseDTO createTransaction(TransactionRequestDTO request);
    List<TransactionResponseDTO> getAllTransactions();
    TransactionResponseDTO getTransactionById(Long id);
    List<TransactionResponseDTO> getTransactionsByAccount(String accountNo);
    List<Transaction> getTransactionByTransactionId(String txnId);
}
