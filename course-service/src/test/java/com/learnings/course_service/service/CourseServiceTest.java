package com.learnings.course_service.service;

import com.learnings.course_service.dto.CourseDTO;
import com.learnings.course_service.entity.Course;
import com.learnings.course_service.mapper.CourseMapper;
import com.learnings.course_service.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private CourseDTO courseDTO;

    @BeforeEach
    void setup() {
        course = new Course();
        course.setId(1L);
        course.setTitle("Spring Boot Fundamentals");

        courseDTO = new CourseDTO(1L, "Spring Boot Fundamentals");
    }

    @Test
    void findById_shouldReturnCourseDTO() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseMapper.toDto(course)).thenReturn(courseDTO);

        CourseDTO result = courseService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(courseRepository).findById(1L);
    }

    @Test
    void findAll_shouldReturnListOfCourses() {
        when(courseRepository.findAll()).thenReturn(List.of(course));
        when(courseMapper.toDtos(anyList())).thenReturn(List.of(courseDTO));

        List<CourseDTO> result = courseService.findAll();

        assertThat(result).hasSize(1);
        verify(courseRepository).findAll();
    }

    @Test
    void save_shouldSaveAndReturnCourseDTO() {
        when(courseMapper.toEntity(courseDTO)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toDto(course)).thenReturn(courseDTO);

        CourseDTO result = courseService.save(courseDTO);

        assertThat(result).isEqualTo(courseDTO);
        verify(courseRepository).save(course);
    }

    @Test
    void findMyCourses_shouldReturnMappedCourses() {
        when(courseRepository.findCoursesByInternId(101L)).thenReturn(List.of(course));
        when(courseMapper.toDtos(List.of(course))).thenReturn(List.of(courseDTO));

        List<CourseDTO> result = courseService.findMyCourses(101L);

        assertThat(result).isNotEmpty();
        verify(courseRepository).findCoursesByInternId(101L);
    }
}

