package com.example.demo.controller;

import com.example.demo.dto.NewsPostRequest;
import com.example.demo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/admin/news")
@RequiredArgsConstructor
public class AdminNewsController {

    private final NotificationService notificationService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_Admin')")
    public ResponseEntity<?> postNews(@RequestBody NewsPostRequest request, Principal principal) {
        notificationService.postNews(request.getMessage(), principal.getName());
        return ResponseEntity.ok("News posted successfully.");
    }

    @GetMapping
    public ResponseEntity<?> getNews() {
        return ResponseEntity.ok(notificationService.getAllNews());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_Admin')")
    public ResponseEntity<?> updateNews(@PathVariable Long id, @RequestBody NewsPostRequest request, Principal principal) {
        notificationService.updateNews(id, request.getMessage(), principal.getName());
        return ResponseEntity.ok("News updated.");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_Admin')")
    public ResponseEntity<?> deleteNews(@PathVariable Long id, Principal principal) {
        notificationService.deleteNews(id, principal.getName());
        return ResponseEntity.ok("News deleted.");
    }


}