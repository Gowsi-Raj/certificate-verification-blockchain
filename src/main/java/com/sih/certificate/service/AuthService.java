package com.sih.certificate.service;

import com.sih.certificate.model.User;
import com.sih.certificate.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password)) // demo only (no hashing)
                .orElse(null);
    }
}
