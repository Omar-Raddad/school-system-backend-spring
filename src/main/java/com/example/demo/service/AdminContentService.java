package com.example.demo.service;

import com.example.demo.model.Content;
import com.example.demo.model.Parent;
import com.example.demo.repository.ContentRepository;
import com.example.demo.repository.ParentRepository;
import com.example.demo.utils.GoogleDriveUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AdminContentService {

    private final ContentRepository contentRepository;
    private final ParentRepository parentRepository;

    @Autowired
    public AdminContentService(ContentRepository contentRepository, ParentRepository parentRepository) {
        this.contentRepository = contentRepository;
        this.parentRepository = parentRepository;
    }

    public void uploadContent(String title, String type, String subject, String grade, String driveUrl, Principal principal) {
        Parent uploader = parentRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!"Admin".equals(uploader.getRole())) {
            throw new AccessDeniedException("Only admins can upload content.");
        }

        Content content = new Content();
        content.setTitle(title);
        content.setType(type);
        content.setSubject(subject);
        content.setGrade(grade);
        content.setDriveUrl(driveUrl);
        content.setCreatedAt(LocalDateTime.now());
        content.setUserId(uploader.getId());

        contentRepository.save(content);
    }


    public List<Content> getAllContent() {
        return contentRepository.findAll();
    }

    public void deleteContent(Integer id) {
        if (!contentRepository.existsById(Long.valueOf(id))) {
            throw new RuntimeException("Content not found with ID: " + id);
        }
        contentRepository.deleteById(Long.valueOf(id));
    }

    public Content getContentById(Integer id) {
        return contentRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new NoSuchElementException("Content with ID " + id + " not found"));
    }

    public Content updateContent(Integer id, String title, String type, String subject) {
        Content content = contentRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Content with ID " + id + " not found"));

        content.setTitle(title);
        content.setType(type);
        content.setSubject(subject);

        return contentRepository.save(content);
    }


}
