package com.sih.certificate.controller;

import com.sih.certificate.dto.LoginRequest;
import com.sih.certificate.model.User;
import com.sih.certificate.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = authService.login(request.getUsername(), request.getPassword());

        if (user == null) {
            return ResponseEntity.status(401).body(
                    java.util.Map.of("error", "Invalid username or password")
            );
        }

        // ✅ Don't return password
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }
}
