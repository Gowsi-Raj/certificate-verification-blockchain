package com.sih.certificate.service;

import com.sih.certificate.model.User;
import com.sih.certificate.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Auto-create demo users only if missing (safe)
    @PostConstruct
    public void ensureDefaults() {
        userRepository.findByUsername("admin")
                .orElseGet(() -> userRepository.save(new User("admin", "admin123", "ADMIN")));

        userRepository.findByUsername("student")
                .orElseGet(() -> userRepository.save(new User("student", "student123", "STUDENT")));
    }

    public User login(String username, String password) {
        if (username == null || password == null) return null;

        String u = username.trim();
        String p = password.trim();

        return userRepository.findByUsername(u)
                .filter(dbUser -> dbUser.getPassword() != null && dbUser.getPassword().trim().equals(p))
                .orElse(null);
    }
}
