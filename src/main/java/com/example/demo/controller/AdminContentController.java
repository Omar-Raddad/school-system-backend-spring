package com.example.demo.controller;

import com.example.demo.model.Content;
import com.example.demo.service.AdminContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/admin/content")
public class AdminContentController {

    private final AdminContentService adminContentService;

    @Autowired
    public AdminContentController(AdminContentService adminContentService) {
        this.adminContentService = adminContentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Content> uploadContent(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("type") String type,
            @RequestParam("subject") String subject
    ) throws Exception {
        Content savedContent = adminContentService.uploadContent(file, title, type, subject);
        return ResponseEntity.ok(savedContent);
    }

    @GetMapping
    public ResponseEntity<List<Content>> getAllContent() {
        List<Content> contents = adminContentService.getAllContent();
        return ResponseEntity.ok(contents);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContent(@PathVariable Integer id) {
        adminContentService.deleteContent(id);
        return ResponseEntity.ok("Content deleted successfully");
    }


}
