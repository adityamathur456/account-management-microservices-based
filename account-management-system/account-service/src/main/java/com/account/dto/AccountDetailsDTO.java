package com.account.dto;

import com.account.enums.AccountStatus;
import com.account.enums.AccountType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDetailsDTO {
    private String accountNo;
    private AccountType accountType;
    private Double balance;
    private AccountStatus status;

    // Embedded Customer details
    private String customerName;
    private String customerEmail;
    private LocalDate customerDateOfBirth;
    private AddressDTO customerAddress;
}
