package com.learnings.course_service.service;

import com.learnings.course_service.dto.CourseDTO;
import com.learnings.course_service.entity.Course;
import com.learnings.course_service.mapper.CourseMapper;
import com.learnings.course_service.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseDTO findById(Long id) {
        return courseMapper.toDto(courseRepository.findById(id).orElse(null));
    }

    public List<CourseDTO> findAll() {
        return courseMapper.toDtos(courseRepository.findAll());
    }

    public CourseDTO save(CourseDTO courseDTO) {
        Course course = courseMapper.toEntity(courseDTO);
        return courseMapper.toDto(courseRepository.save(course));
    }

    public List<CourseDTO> findMyCourses(Long id) {
        return courseMapper.toDtos(courseRepository.findCoursesByInternId(id));
    }
}
