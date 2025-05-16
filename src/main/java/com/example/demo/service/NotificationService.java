package com.example.demo.service;


import com.example.demo.model.Notification;
import com.example.demo.model.Parent;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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


    public List<Notification> getAllNews() {
        return notificationRepository.findByType("NEWS");
    }
}
