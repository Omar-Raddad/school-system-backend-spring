package com.example.demo.controller;

import com.example.demo.dto.FeedbackRequest;
import com.example.demo.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/api/feedback")
    @PreAuthorize("hasAuthority('ROLE_Parent')")
    public ResponseEntity<?> submitFeedback(@RequestBody FeedbackRequest request, Principal principal) {
        feedbackService.submitFeedback(request, principal.getName());
        return ResponseEntity.ok("Feedback submitted successfully.");
    }

    @GetMapping("/api/admin/feedback")
    @PreAuthorize("hasAuthority('ROLE_Admin')")
    public ResponseEntity<?> viewAllFeedback() {
        return ResponseEntity.ok(feedbackService.getAllFeedback());
    }
}
