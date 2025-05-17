package com.example.demo.controller;

import com.example.demo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class PublicNewsController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> getNews() {
        return ResponseEntity.ok(notificationService.getAllNews());
    }
}
