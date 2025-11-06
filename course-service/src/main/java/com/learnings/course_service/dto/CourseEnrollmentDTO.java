package com.learnings.course_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseEnrollmentDTO {
    private Long id;
    private CourseDTO course;
    private Long internId;
    private String status;
    private LocalDateTime enrolledAt;
}
