// src/main/java/com/auth/repository/UserRepository.java
package com.auth.repository;

import com.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<User> findByCustomerId(String customerId);
    boolean existsByEmail(String email);
    // new method to search by either
    default Optional<User> findByUsernameOrCustomerId(String input) {
        Optional<User> user = findByUsername(input);
        if (user.isPresent()) return user;
        return findByCustomerId(input);
    }
}
