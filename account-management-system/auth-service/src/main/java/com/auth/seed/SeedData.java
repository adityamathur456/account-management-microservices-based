// src/main/java/com/bank/auth/bootstrap/SeedData.java
package com.auth.seed;

import com.auth.domain.Role;
import com.auth.domain.User;
import com.auth.repository.RoleRepository;
import com.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class SeedData implements CommandLineRunner {
    private final RoleRepository roles; private final UserRepository users; private final PasswordEncoder enc;

    @Override public void run(String... args) {
        Role admin = roles.findByName("ROLE_ADMIN").orElseGet(() -> roles.save(Role.builder().name("ROLE_ADMIN").build()));
        Role user  = roles.findByName("ROLE_USER").orElseGet(() -> roles.save(Role.builder().name("ROLE_USER").build()));
        if (users.findByUsername("admin").isEmpty()) {
            users.save(User.builder()
                    .username("admin").email("admin@bank.local")
                    .passwordHash(enc.encode("Admin@1234"))
                    .roles(Set.of(admin, user)).enabled(true).build());
        }
    }
}
