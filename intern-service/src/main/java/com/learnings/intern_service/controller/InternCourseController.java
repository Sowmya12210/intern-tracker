package com.learnings.intern_service.controller;

import com.learnings.intern_service.client.CourseClient;
import com.learnings.intern_service.dto.CourseDTO;
import com.learnings.intern_service.dto.CourseEnrollmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/api/interns/courses")
public class InternCourseController {
    private final CourseClient  courseClient;

    @GetMapping
    public List<CourseDTO> getAllCourses() {
        return courseClient.findAll();
    }

    @GetMapping("/mine")
    public List<CourseDTO> getMyCourses(@RequestHeader("X-User-Id") Long userId) {
        return courseClient.findMyCourses(userId);
    }

    @PostMapping("/{id}/enroll")
    public CourseEnrollmentDTO enrollCourse(@RequestHeader("X-User-Id") Long userId, @PathVariable Long id) {
        return courseClient.enrollCourse(userId, id);
    }
}
