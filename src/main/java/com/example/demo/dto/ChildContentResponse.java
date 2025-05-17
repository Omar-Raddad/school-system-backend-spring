package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChildContentResponse {
    private String childName;
    private String grade;
    private List<ContentItem> content;
}
