package com.example.demo.service;

import com.example.demo.model.Parent;
import com.example.demo.repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final ParentRepository parentRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public void initiatePasswordReset(String email) {
        Parent parent = parentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String resetToken = UUID.randomUUID().toString();
        parent.setResetCode(resetToken);
        parent.setResetCodeExpiration(LocalDateTime.now().plusHours(1));
        parent.setResetCodeVerified(false);
        parentRepository.save(parent);

        sendResetEmail(parent.getEmail(), resetToken);
    }

    private void sendResetEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset Request");
        message.setText("Use this token to reset your password: " + token +
                "\nExpires in 1 hour.");
        mailSender.send(message);
    }

    public void resetPassword(String token, String newPassword) {
        Parent parent = parentRepository.findByResetCode(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (LocalDateTime.now().isAfter(parent.getResetCodeExpiration())) {
            throw new RuntimeException("Token expired");
        }

        parent.setPassword(passwordEncoder.encode(newPassword));
        parent.setResetCode(null);
        parent.setResetCodeExpiration(null);
        parent.setResetCodeVerified(true);
        parentRepository.save(parent);
    }
}