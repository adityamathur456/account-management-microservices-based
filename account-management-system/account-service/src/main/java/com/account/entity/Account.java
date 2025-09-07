package com.account.entity;

import com.account.enums.AccountStatus;
import com.account.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // internal DB id
    private Long id;

    @Column(name = "account_no", unique = true, nullable = false, length = 12)
    private String accountNo;

    @Column(name = "customer_id", nullable = false)
    private String customerId;  // link to customer-service

    @Column(name = "balance")
    private Double balance;

    @Column(name = "account_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType; // e.g., SAVINGS, CURRENT, FIXED_DEPOSIT, RECURRING_DEPOSIT

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status; // ACTIVE, INACTIVE, CLOSED, BLOCKED
}
