package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "parents")
@Data
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @Column(name = "reset_code")
    private String resetCode;

    @Column(name = "reset_code_expiration")
    private LocalDateTime resetCodeExpiration;

    @Column(name = "reset_code_verified")
    private boolean resetCodeVerified;
    //private String resetCode;
   // private LocalDateTime resetCodeExpiration;
    //private boolean resetCodeVerified;

    @Column(nullable = false)
    private String role; // Parent, Admin, Childreen

    private String verificationCode;
    private LocalDateTime verificationCodeExpiration;
    private boolean verified;

    @Column(name = "children_count", nullable = false)
    private int childrenCount;

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }


    public String getResetCode() {
        return resetCode;
    }

    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
    }

    public LocalDateTime getResetCodeExpiration() {
        return resetCodeExpiration;
    }

    public void setResetCodeExpiration(LocalDateTime resetCodeExpiration) {
        this.resetCodeExpiration = resetCodeExpiration;
    }

    public boolean isResetCodeVerified() {
        return resetCodeVerified;
    }

    public void setResetCodeVerified(boolean resetCodeVerified) {
        this.resetCodeVerified = resetCodeVerified;
    }
}