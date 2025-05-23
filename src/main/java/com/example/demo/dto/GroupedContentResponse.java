package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroupedContentResponse {
    private String grade;
    private List<SubjectGroup> subjects;
}