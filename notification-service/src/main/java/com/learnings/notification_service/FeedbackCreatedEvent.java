package com.learnings.notification_service;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackCreatedEvent {
    private Long feedbackId;
    private Long mentorId;
    private String mentorName;
    private Long internId;
    private String message;
    private Integer score;
    private LocalDateTime createdAt;
}

