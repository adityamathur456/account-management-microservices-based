package com.account.repository;

import com.account.dto.AccountDetailsDTO;
import com.account.entity.Account;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNo(String accountNo);

    Optional<Account> findByCustomerId(String customerId);

    AccountDetailsDTO findAccountDetailsByCustomerId(String customerId);

    @Transactional
    long deleteByAccountNo(String accountNo);
}
