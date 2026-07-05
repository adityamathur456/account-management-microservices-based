package com.transaction.dto;

import com.transaction.enums.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransactionRequestDTO {

    @NotNull(message = "Source account number is required")
    private String sourceAccountNo;

    @NotNull(message = "Destination account number is required")
    private String destinationAccountNo;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Transaction amount must be greater than 0")
    private Double amount;

    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;

    private String remarks;
}
