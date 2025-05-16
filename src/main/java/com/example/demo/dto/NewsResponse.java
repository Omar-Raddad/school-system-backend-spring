package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NewsResponse {
    private Long id;
    private String message;
    private String adminName;
    private LocalDateTime createdAt;
}
