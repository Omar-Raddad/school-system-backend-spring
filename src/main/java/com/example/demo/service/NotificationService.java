package com.example.demo.service;

import com.example.demo.dto.NewsResponse;
import com.example.demo.model.Notification;
import com.example.demo.model.Parent;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ParentRepository parentRepository;

    public void postNews(String message, String adminEmail) {
        Parent admin = parentRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Notification news = Notification.builder()
                .userId(admin.getId())
                .type("NEWS")
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(news);
    }

    public List<NewsResponse> getAllNews() {
        List<Notification> newsList = notificationRepository.findByType("NEWS");

        return newsList.stream().map(news -> {
            AtomicReference<String> nameRef = new AtomicReference<>("Unknown");
            if (news.getUserId() != null) {
                parentRepository.findById(news.getUserId()).ifPresent(admin -> {
                    nameRef.set(admin.getName());
                });
            }
            return new NewsResponse(news.getId(), news.getMessage(), nameRef.get(), news.getCreatedAt());
        }).toList();
    }

    public void updateNews(Long id, String newMessage, String adminEmail) {
        Notification news = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));

        Parent admin = parentRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!news.getUserId().equals(admin.getId())) {
            throw new AccessDeniedException("You can only update your own posts.");
        }

        news.setMessage(newMessage);
        notificationRepository.save(news);
    }

    public void deleteNews(Long id, String adminEmail) {
        Notification news = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));

        Parent admin = parentRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!news.getUserId().equals(admin.getId())) {
            throw new AccessDeniedException("You can only delete your own posts.");
        }

        notificationRepository.deleteById(id);
    }

}
