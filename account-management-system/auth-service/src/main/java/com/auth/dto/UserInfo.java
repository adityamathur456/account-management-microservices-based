package com.auth.dto;


public record UserInfo(Long id, String username, String email, String customerId,java.util.Set<String> roles) {}