package com.auth.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterRequest(
        @NotBlank String username,
        @Email @NotBlank String email,
        @Size(min = 8) String password,

        // Customer fields
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
        @NotBlank String phoneNumber,
        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,
        @NotBlank String street,
        @NotBlank String houseNo,
        @NotBlank String city,
        @NotBlank String state,
        @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be 6 digits") String pincode
) {}
