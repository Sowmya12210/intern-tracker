package com.learnings.course_service.controller;

import com.learnings.course_service.dto.CourseDTO;
import com.learnings.course_service.entity.Course;
import com.learnings.course_service.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CourseDTO> findAll() {
        return courseService.findAll();
    }

    @PostMapping
    public CourseDTO save(@RequestBody CourseDTO course) {
        return courseService.save(course);
    }

    @GetMapping("/{id}")
    public CourseDTO findById(@PathVariable Long id) {
        return courseService.findById(id);
    }

    @GetMapping("/mine")
    public List<CourseDTO> findMyCourses(@RequestHeader("X-User-Id") Long userId) {
        return courseService.findMyCourses(userId);
    }
}
