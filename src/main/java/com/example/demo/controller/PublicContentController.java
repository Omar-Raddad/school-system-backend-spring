package com.example.demo.controller;

import com.example.demo.service.AdminContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class PublicContentController {

    private final AdminContentService contentService;

    @GetMapping("/by-grade")
    public ResponseEntity<?> getContentGrouped() {
        return ResponseEntity.ok(contentService.getContentGroupedByGradeAndSubject());
    }
}
