package com.account.dto;

import com.account.enums.AccountStatus;
import com.account.enums.AccountType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {
    private Long id;
    private String accountNo;
    private String customerId;
    private Double balance;
    private AccountType accountType;
    private AccountStatus status;
}
