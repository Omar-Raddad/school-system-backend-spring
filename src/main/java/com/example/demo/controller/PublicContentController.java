package com.example.demo.controller;

import com.example.demo.service.AdminContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class PublicContentController {

    private final AdminContentService contentService;

    @GetMapping("/by-grade")
    public ResponseEntity<?> getContentGrouped() {
        return ResponseEntity.ok(contentService.getContentGroupedByGradeAndSubject());
    }

    @GetMapping("/available")
    @PreAuthorize("hasAuthority('ROLE_Parent')")
    public ResponseEntity<?> getAvailableContent(Principal principal) {
        return ResponseEntity.ok(contentService.getContentForParent(principal.getName()));
    }

}
