package com.learnings.intern_service.client;

import com.learnings.intern_service.dto.CourseDTO;
import com.learnings.intern_service.dto.CourseEnrollmentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "course-service", path = "/api/courses")
public interface CourseClient {
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<CourseDTO> findAll();

    @GetMapping("/{id}")
    CourseDTO getCourseById(@PathVariable("id") Long id);

    @PostMapping("/{id}/enroll")
    CourseEnrollmentDTO enrollCourse(@RequestHeader("X-User-Id") Long userId, @PathVariable Long id);

    @GetMapping("/mine")
    List<CourseDTO> findMyCourses(@RequestHeader("X-User-Id") Long userId);
}
