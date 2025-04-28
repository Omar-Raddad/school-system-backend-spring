package com.example.demo.service;

import com.example.demo.model.Content;
import com.example.demo.repository.ContentRepository;
import com.example.demo.utils.GoogleDriveUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AdminContentService {

    private final ContentRepository contentRepository;

    @Autowired
    public AdminContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public Content uploadContent(MultipartFile file, String title, String type, String subject) throws Exception {
        // 1. Upload file to Google Drive
        String driveUrl = GoogleDriveUploader.uploadFile(file);

        // 2. Save to database
        Content content = Content.builder()
                .title(title)
                .type(type)
                .subject(subject)
                .driveUrl(driveUrl)
                .build();

        return contentRepository.save(content);
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

}
