// src/main/java/com/auth/domain/User.java
package com.auth.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="users", uniqueConstraints={
        @UniqueConstraint(columnNames="username"),
        @UniqueConstraint(columnNames="email"),
        @UniqueConstraint(columnNames="customer_id") // ensure uniqueness
})
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=50)
    private String username;

    @Column(nullable=false)
    private String passwordHash;

    @Column(nullable=false, length=120)
    private String email;

    @Column(name="customer_id", length=15)
    private String customerId; // <-- new field to store related customer

    private boolean enabled = true;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="user_roles",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_id"))
    private Set<Role> roles;
}

