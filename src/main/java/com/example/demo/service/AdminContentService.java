package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.exception.NoContentAvailableException;
import com.example.demo.model.Child;
import com.example.demo.model.Content;
import com.example.demo.model.Parent;
import com.example.demo.repository.ChildRepository;
import com.example.demo.repository.ContentRepository;
import com.example.demo.repository.ParentRepository;
import com.example.demo.repository.SubscriptionRepository;
import com.example.demo.utils.GoogleDriveUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AdminContentService {

    private final ContentRepository contentRepository;
    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public AdminContentService(ContentRepository contentRepository, ParentRepository parentRepository, ChildRepository childRepository, SubscriptionRepository subscriptionRepository) {
        this.contentRepository = contentRepository;
        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
        this.subscriptionRepository = subscriptionRepository;
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

    public void deleteContent(Long contentId, String adminEmail) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content not found"));

        Parent admin = parentRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!content.getUserId().equals(admin.getId())) {
            throw new AccessDeniedException("You can only delete content you uploaded.");
        }

        contentRepository.delete(content);
    }

    public Content getContentById(Integer id) {
        return contentRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new NoSuchElementException("Content with ID " + id + " not found"));
    }

    public void updateContent(Long contentId, ContentUpdateRequest request, String adminEmail) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content not found"));

        Parent admin = parentRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!content.getUserId().equals(admin.getId())) {
            throw new AccessDeniedException("You can only update content you uploaded.");
        }

        content.setTitle(request.getTitle());
        content.setType(request.getType());
        content.setSubject(request.getSubject());
        content.setGrade(request.getGrade());

        contentRepository.save(content);
    }

    public List<GroupedContentResponse> getContentGroupedByGradeAndSubject() {
        List<Content> all = contentRepository.findAll();

        Map<String, Map<String, List<Content>>> grouped = all.stream()
                .collect(Collectors.groupingBy(
                        Content::getGrade,
                        Collectors.groupingBy(Content::getSubject)
                ));

        List<GroupedContentResponse> result = new ArrayList<>();

        for (Map.Entry<String, Map<String, List<Content>>> gradeEntry : grouped.entrySet()) {
            List<SubjectGroup> subjectGroups = new ArrayList<>();
            for (Map.Entry<String, List<Content>> subjectEntry : gradeEntry.getValue().entrySet()) {
                List<ContentItem> items = subjectEntry.getValue().stream()
                        .map(c -> new ContentItem(c.getId(), c.getTitle(), c.getType(), c.getDriveUrl(), c.getCreatedAt()))
                        .toList();
                subjectGroups.add(new SubjectGroup(subjectEntry.getKey(), items));
            }
            result.add(new GroupedContentResponse(gradeEntry.getKey(), subjectGroups));
        }

        return result;
    }


    public List<ChildContentResponse> getContentForParent(String parentEmail) {
        Parent parent = parentRepository.findByEmail(parentEmail)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        List<Child> children = childRepository.findByParentId(parent.getId());
        List<ChildContentResponse> result = new ArrayList<>();

        for (Child child : children) {
            boolean hasActiveSub = subscriptionRepository.existsByUserIdAndChildIdAndIsActiveTrue(
                    parent.getId(), child.getId()
            );

            if (!hasActiveSub) continue;

            List<Content> contentList = contentRepository.findByGrade(child.getGrade());

            List<ContentItem> items = contentList.stream()
                    .map(c -> new ContentItem(c.getId(), c.getTitle(), c.getType(), c.getDriveUrl(), c.getCreatedAt()))
                    .toList();

            result.add(new ChildContentResponse(
                    child.getName(),
                    child.getGrade(),
                    items
            ));
        }

        if (result.isEmpty()) {
            throw new NoContentAvailableException("No content available. Please check your subscription.");
        }
        return result;

    }


}
