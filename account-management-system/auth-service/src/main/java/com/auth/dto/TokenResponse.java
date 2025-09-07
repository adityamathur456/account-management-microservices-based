package com.auth.dto;


import lombok.*;

@Builder
public record TokenResponse(
        String customerId,
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresInSeconds
){}


