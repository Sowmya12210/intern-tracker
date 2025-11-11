package com.learnings.course_service.service;

import com.learnings.course_service.dto.CourseDTO;
import com.learnings.course_service.entity.Course;
import com.learnings.course_service.entity.CourseEnrollment;
import com.learnings.course_service.mapper.CourseMapper;
import com.learnings.course_service.repository.CourseEnrollementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseEnrollmentServiceTest {

    @Mock
    private CourseEnrollementRepository repository;

    @Mock
    private CourseService courseService;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseEnrollementService courseEnrollementService;

    private CourseDTO courseDTO;
    private Course course;

    @BeforeEach
    void setup() {
        courseDTO = new CourseDTO(1L, "Docker Deep Dive");
        course = new Course();
        course.setId(1L);
        course.setTitle("Docker Deep Dive");
    }

    @Test
    void enrollCourse_shouldSaveEnrollment_whenCourseExists() {
        when(courseService.findById(1L)).thenReturn(courseDTO);
        when(courseMapper.toEntity(courseDTO)).thenReturn(course);
        when(repository.save(any(CourseEnrollment.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        CourseEnrollment result = courseEnrollementService.enrollCourse(42L, 1L);

        assertThat(result.getInternId()).isEqualTo(42L);
        assertThat(result.getCourse().getId()).isEqualTo(1L);
        verify(repository).save(any(CourseEnrollment.class));
    }

    @Test
    void enrollCourse_shouldThrow_whenCourseNotFound() {
        when(courseService.findById(99L)).thenReturn(null);

        assertThatThrownBy(() -> courseEnrollementService.enrollCourse(42L, 99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Course not found");

        verify(repository, never()).save(any());
    }
}

