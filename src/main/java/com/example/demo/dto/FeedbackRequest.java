package com.example.demo.dto;

import lombok.Data;

@Data
public class FeedbackRequest {
    private Long childId;
    private String message;
    private Integer rating;
}
