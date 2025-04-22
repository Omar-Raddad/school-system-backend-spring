package com.example.demo.controller;

import com.example.demo.model.Parent;
import com.example.demo.repository.ParentRepository;
import com.example.demo.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // Proper signup endpoint
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Parent parent) {
        // Check for existing email
        if (parentRepository.existsByEmail(parent.getEmail())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        // Hash password
        parent.setPassword(passwordEncoder.encode(parent.getPassword()));
        parent.setRole("Parent");
        parent.setVerified(false);
        parent.setChildrenCount(0);

        Parent savedParent = parentRepository.save(parent);
        return ResponseEntity.ok(savedParent);
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Parent parent = parentRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), parent.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid password");
        }

        // Generate JWT
        String token = jwtService.generateToken(parent);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "role", parent.getRole()
        ));
    }

    // DTO for login request

    private static class LoginRequest {
        private String email;
        private String password;
        // Getters and setters

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}