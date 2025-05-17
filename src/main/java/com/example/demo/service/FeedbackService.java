package com.example.demo.service;

import com.example.demo.dto.FeedbackRequest;
import com.example.demo.model.Feedback;
import com.example.demo.model.Parent;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ParentRepository parentRepository;

    public void submitFeedback(FeedbackRequest request, String parentEmail) {
        Parent parent = parentRepository.findByEmail(parentEmail)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        Feedback feedback = new Feedback();
        feedback.setParentId(parent.getId());
        feedback.setChildId(request.getChildId());
        feedback.setMessage(request.getMessage());
        feedback.setRating(request.getRating());
        feedback.setCreatedAt(LocalDateTime.now());

        feedbackRepository.save(feedback);
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc();
    }
}
