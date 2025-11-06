package com.learnings.course_service.mapper;

import com.learnings.course_service.dto.CourseDTO;
import com.learnings.course_service.entity.Course;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseDTO toDto(Course course);
    Course toEntity(CourseDTO courseDTO);
    List<CourseDTO> toDtos(List<Course> courses);
}
