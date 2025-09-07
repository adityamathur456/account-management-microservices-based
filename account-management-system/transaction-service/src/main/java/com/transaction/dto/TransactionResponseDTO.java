package com.transaction.dto;

import com.transaction.enums.TransactionStatus;
import com.transaction.enums.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionResponseDTO {

    private String transactionId;
    private String sourceAccountNo;
    private String destinationAccountNo;
    private Double amount;
    private TransactionType transactionType;
    private TransactionStatus status;
    private LocalDateTime transactionTime;
    private String remarks;
}
