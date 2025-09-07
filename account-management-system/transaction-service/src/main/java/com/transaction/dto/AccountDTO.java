package com.transaction.dto;

import com.transaction.enums.AccountStatus;
import com.transaction.enums.AccountType;
import lombok.Data;

@Data
public class AccountDTO {
    private String accountNo;
    private String customerId;
    private Double balance;
    private AccountType accountType;
    private AccountStatus status;
}
