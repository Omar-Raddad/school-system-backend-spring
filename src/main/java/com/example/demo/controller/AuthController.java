// AuthController.java (src/main/java/com/example/demo/controller/AuthController.java)
package com.example.demo.controller;

import com.example.demo.model.Parent;
import com.example.demo.repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private ParentRepository parentRepository;

    // Simple test endpoint to check database connection
    @GetMapping("/test-db")
    public String testDatabase() {
        // Create a test parent
        Parent parent = new Parent();
        parent.setEmail("test@example.com");
        parent.setName("Test User");
        parent.setPassword("test123");
        parent.setPhoneNumber("+1234567890");
        parent.setRole("Parent");
        parent.setVerified(false);
        parent.setChildrenCount(0);

        // Save to database
        parentRepository.save(parent);
        return "Database connection works! Check PostgreSQL for the new parent.";
    }
}