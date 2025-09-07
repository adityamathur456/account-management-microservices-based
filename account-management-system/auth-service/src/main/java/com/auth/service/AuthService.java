// src/main/java/com/bank/auth/service/AuthService.java
package com.auth.service;

import com.auth.client.CustomerClient;
import com.auth.domain.*;
import com.auth.dto.*;
import com.auth.repository.*;
import com.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final RefreshTokenRepository refreshRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final CustomerClient customerClient; // Inject Feign Client

    @Value("${security.jwt.refresh-token-ttl-days:7}")
    private long refreshDays;

    @Value("${security.jwt.access-token-ttl-min:15}")
    private long accessTokenTtlMinutes;

    public RegisterResponse register(RegisterRequest req) {
        if (userRepo.existsByUsername(req.username())) throw new IllegalArgumentException("username taken");
        if (userRepo.existsByEmail(req.email())) throw new IllegalArgumentException("email taken");

        Role userRole = roleRepo.findByName("ROLE_USER")
                .orElseGet(() -> roleRepo.save(Role.builder().name("ROLE_USER").build()));

        // 1️⃣ Create User first
        User u = User.builder()
                .username(req.username())
                .email(req.email())
                .passwordHash(encoder.encode(req.password()))
                .roles(Set.of(userRole))
                .enabled(true)
                .build();

        // 2️⃣ Generate temporary token for customer-service call
        String tempToken = "Bearer " + jwt.generateAccessToken(
                u.getUsername(),
                u.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );

        // 3️⃣ Create Customer
        CustomerDTO customerDTO = CustomerDTO.builder()
                .firstName(req.firstName())
                .lastName(req.lastName())
                .email(req.email())
                .phoneNumber(req.phoneNumber())
                .dateOfBirth(req.dateOfBirth())
                .address(AddressDTO.builder()
                        .street(req.street())
                        .houseNo(req.houseNo())
                        .city(req.city())
                        .state(req.state())
                        .pincode(req.pincode())
                        .build())
                .build();

        CustomerDTO createdCustomer = customerClient.createCustomer(tempToken, customerDTO);

        // 4️⃣ Link customerId to User
        u.setCustomerId(createdCustomer.getId());
        System.out.println((u.getCustomerId()) + "   THis is customer id");
        userRepo.save(u);

        // 5️⃣ Generate final tokens for the response
        String accessToken = jwt.generateAccessToken(
                u.getUsername(),
                u.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );
        String refreshToken = issueRefresh(u);

        long exp = java.time.Duration.ofMinutes(
                Long.parseLong(System.getProperty("security.jwt.access-token-ttl-min", "30"))
        ).toSeconds();

        return RegisterResponse.builder()
                .userId(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .customerId(createdCustomer.getId())
                .tokenData(TokenResponse.builder()
                        .customerId(u.getCustomerId())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .tokenType("Bearer")
                        .expiresInSeconds(exp)
                        .build()
                )
                .build();
    }


    public TokenResponse login(LoginRequest req) {
        // Try to find by username OR customerId
        User u = userRepo.findByUsernameOrCustomerId(req.username())
                .orElseThrow(() -> new IllegalArgumentException("invalid credentials"));

        if (!u.isEnabled() || !encoder.matches(req.password(), u.getPasswordHash()))
            throw new IllegalArgumentException("invalid credentials");
        String custId = u.getCustomerId();
        String access = jwt.generateAccessToken(u.getUsername(),
                u.getRoles().stream().map(Role::getName).collect(java.util.stream.Collectors.toSet()));
        String refresh = issueRefresh(u);
        long exp = java.time.Duration.ofMinutes(accessTokenTtlMinutes).toSeconds();

        return new TokenResponse(custId, access, refresh, "Bearer", exp);
    }


    public TokenResponse refresh(RefreshRequest req) {
        RefreshToken rt = refreshRepo.findByToken(req.refreshToken())
                .orElseThrow(() -> new IllegalArgumentException("invalid refresh"));
        if (rt.isRevoked() || rt.getExpiresAt().isBefore(Instant.now()))
            throw new IllegalArgumentException("expired refresh");
        User u = rt.getUser();
        String custId = u.getCustomerId();
        String access = jwt.generateAccessToken(u.getUsername(),
                u.getRoles().stream().map(Role::getName).collect(java.util.stream.Collectors.toSet()));
        return new TokenResponse(custId ,access, rt.getToken(), "Bearer",
                java.time.Duration.ofMinutes(15).toSeconds());
    }

    public void revoke(String refreshToken) {
        refreshRepo.findByToken(refreshToken).ifPresent(rt -> {
            rt.setRevoked(true); refreshRepo.save(rt);
        });
    }

    private String issueRefresh(User u) {
        String token = UUID.randomUUID().toString();
        RefreshToken rt = RefreshToken.builder()
                .token(token)
                .user(u)
                .expiresAt(Instant.now().plus(java.time.Duration.ofDays(refreshDays)))
                .revoked(false).build();
        refreshRepo.save(rt);
        return token;
    }
}
