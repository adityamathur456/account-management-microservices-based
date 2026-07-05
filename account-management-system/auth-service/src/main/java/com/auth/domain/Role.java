// src/main/java/com/auth/domain/Role.java
package com.auth.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name="roles", uniqueConstraints=@UniqueConstraint(columnNames="name"))
public class Role {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, length=50) private String name; // e.g. ROLE_ADMIN, ROLE_USER
}
