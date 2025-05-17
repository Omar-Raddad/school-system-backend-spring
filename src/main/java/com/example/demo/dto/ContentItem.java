package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ContentItem {
    private Long id;
    private String title;
    private String type;
    private String driveUrl;
    private LocalDateTime createdAt;
}
