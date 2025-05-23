package com.example.demo.controller;

import com.example.demo.dto.ContentUpdateRequest;
import com.example.demo.model.Content;
import com.example.demo.service.AdminContentService;
import com.example.demo.utils.GoogleDriveUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/admin/content")
public class AdminContentController {

    private final AdminContentService adminContentService;
    private final GoogleDriveUploader driveUploader;
    private final AdminContentService contentService;


    @Autowired
    public AdminContentController(AdminContentService adminContentService, GoogleDriveUploader driveUploader, AdminContentService contentService) {
        this.adminContentService = adminContentService;
        this.driveUploader = driveUploader;
        this.contentService = contentService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('ROLE_Admin')")
    public ResponseEntity<?> uploadContent(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("type") String type,
            @RequestParam("subject") String subject,
            @RequestParam("grade") String grade,
            Principal principal
    ) throws GeneralSecurityException, IOException {
        String driveUrl = driveUploader.uploadFile(file);
        adminContentService.uploadContent(title, type, subject, grade, driveUrl, principal);
        return ResponseEntity.ok("Content uploaded successfully.");
    }


    @GetMapping
    public ResponseEntity<List<Content>> getAllContent() {
        List<Content> contents = adminContentService.getAllContent();
        return ResponseEntity.ok(contents);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_Admin')")
    public ResponseEntity<?> deleteContent(
            @PathVariable Long id,
            Principal principal
    ) {
        adminContentService.deleteContent(id, principal.getName());
        return ResponseEntity.ok("Content deleted successfully.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Content> getContentById(@PathVariable Integer id) {
        Content content = adminContentService.getContentById(id);
        return ResponseEntity.ok(content);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_Admin')")
    public ResponseEntity<?> updateContent(
            @PathVariable Long id,
            @RequestBody ContentUpdateRequest request,
            Principal principal
    ) {
        adminContentService.updateContent(id, request, principal.getName());
        return ResponseEntity.ok("Content updated successfully.");
    }


    @PreAuthorize("hasAuthority('ROLE_Admin')")
    @GetMapping("/by-grade")
    public ResponseEntity<?> getAllContentForAdmin() {
        return ResponseEntity.ok(contentService.getContentGroupedByGradeAndSubject());
    }
}
