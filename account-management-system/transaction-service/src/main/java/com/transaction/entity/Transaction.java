package com.transaction.entity;

import com.transaction.enums.TransactionStatus;
import com.transaction.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto increment ID
    private Long id;

    @Column(name = "transaction_id", unique = true, nullable = false, length = 36)
    private String transactionId; // Custom generated (UUID or sequence-based)

    @NotNull(message = "Source account number is required")
    @Column(name = "source_account_no", nullable = false, length = 12)
    private String sourceAccountNo;

    @NotNull(message = "Destination account number is required")
    @Column(name = "destination_account_no", nullable = false, length = 12)
    private String destinationAccountNo;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Transaction amount must be greater than 0")
    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 20)
    private TransactionType transactionType; // DEBIT, CREDIT, TRANSFER

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TransactionStatus status; // SUCCESS, FAILED, PENDING

    @CreationTimestamp
    @Column(name = "transaction_time", nullable = false, updatable = false)
    private LocalDateTime transactionTime;

    @Column(length = 500) // or use @Lob for very long text
    private String remarks;
}
