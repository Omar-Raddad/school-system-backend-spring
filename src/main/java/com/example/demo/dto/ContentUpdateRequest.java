package com.example.demo.dto;

import lombok.Data;

@Data
public class ContentUpdateRequest {
    private String title;
    private String type;
    private String subject;
    private String grade;
}