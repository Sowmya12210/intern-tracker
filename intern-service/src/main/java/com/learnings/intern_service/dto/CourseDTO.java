package com.learnings.intern_service.dto;

import lombok.Data;


@Data
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private Integer durationInHours;
    private String tags;
}
