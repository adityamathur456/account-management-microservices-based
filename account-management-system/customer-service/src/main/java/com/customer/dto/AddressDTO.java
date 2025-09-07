package com.customer.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {
    private String street;
    private String houseNo;
    private String city;
    private String state;
    private String pincode;
}
