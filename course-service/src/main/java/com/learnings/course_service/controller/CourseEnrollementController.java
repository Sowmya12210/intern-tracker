package com.learnings.course_service.controller;

import com.learnings.course_service.entity.CourseEnrollment;
import com.learnings.course_service.service.CourseEnrollementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseEnrollementController {
    private final CourseEnrollementService courseEnrollementService;

    @PostMapping("/{courseId}/enroll")
    public CourseEnrollment enrollCourse(@RequestHeader("X-User-Id") Long userId, @PathVariable Long courseId) {
        return courseEnrollementService.enrollCourse(userId, courseId);
    }
}
