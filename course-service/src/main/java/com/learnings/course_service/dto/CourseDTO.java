package com.learnings.course_service.dto;

import lombok.Data;

@Data
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private Integer durationInHours;
    private String tags;

    public CourseDTO(Long l, String s) {
        this.id = l;
        this.title = s;
    }
}
