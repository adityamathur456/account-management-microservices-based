package com.transaction.repository;

import com.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySourceAccountNo(String sourceAccountNo);

    List<Transaction> findByDestinationAccountNo(String destinationAccountNo);

    List<Transaction> findByTransactionId(String transactionId);
}
