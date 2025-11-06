package com.learnings.course_service.service;

import com.learnings.course_service.dto.CourseDTO;
import com.learnings.course_service.entity.Course;
import com.learnings.course_service.entity.CourseEnrollment;
import com.learnings.course_service.mapper.CourseMapper;
import com.learnings.course_service.repository.CourseEnrollementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseEnrollementService {
    private final CourseEnrollementRepository repository;
    private final CourseService courseService;
    private final CourseMapper courseMapper;

    public CourseEnrollment enrollCourse(Long userId, Long courseId) {
        CourseDTO course = courseService.findById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        } else {
            CourseEnrollment courseEnrollment = new CourseEnrollment(userId, courseMapper.toEntity(course));
            return repository.save(courseEnrollment);
        }
    }
}
