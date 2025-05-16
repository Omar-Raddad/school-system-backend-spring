package com.example.demo.controller;

import com.example.demo.dto.SubscriptionRequest;
import com.example.demo.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    @PreAuthorize("hasAuthority('ROLE_Parent')")
    public ResponseEntity<?> subscribe(@RequestBody SubscriptionRequest request, Principal principal) {
        try {
            subscriptionService.subscribeParent(request, principal.getName());
            return ResponseEntity.ok("Subscribed successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during subscription.");
        }
    }

}
