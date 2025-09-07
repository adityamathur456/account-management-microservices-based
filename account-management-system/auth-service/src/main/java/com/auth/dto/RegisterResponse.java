package com.auth.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class RegisterResponse {
    private Long userId;
    private String username;
    private String email;
    private String customerId;
    private TokenResponse tokenData;
}
