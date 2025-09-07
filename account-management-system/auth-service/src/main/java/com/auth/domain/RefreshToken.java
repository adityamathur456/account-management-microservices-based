// src/main/java/com/auth/domain/RefreshToken.java
package com.auth.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name="refresh_tokens", indexes=@Index(columnList="token", unique=true))
public class RefreshToken {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true, length=256) private String token;
    @ManyToOne(optional=false) private User user;
    @Column(nullable=false) private Instant expiresAt;
    private boolean revoked = false;
}
