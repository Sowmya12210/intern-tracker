package com.learnings.course_service.integration_test;

import com.learnings.course_service.dto.CourseDTO;
import com.learnings.course_service.entity.Course;
import com.learnings.course_service.repository.CourseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CourseIntegrationTest {

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void createAndGetCourse_throughApi() {
        CourseDTO dto = new CourseDTO(1L, "Integration Testing with Spring");

        ResponseEntity<CourseDTO> postResponse = restTemplate.postForEntity("/api/courses", dto, CourseDTO.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        CourseDTO saved = postResponse.getBody();
        assertThat(saved).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Integration Testing with Spring");

        ResponseEntity<CourseDTO> getResponse = restTemplate.getForEntity("/api/courses/" + saved.getId(), CourseDTO.class);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getId()).isEqualTo(saved.getId());
    }

    @Test
    void getAllCourses_returnsList() {
        courseRepository.save(new Course(1L, "Course A","",12, ""));
        courseRepository.save(new Course(2L, "Course B","",12, ""));

        ResponseEntity<CourseDTO[]> response = restTemplate.getForEntity("/api/courses", CourseDTO[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        assertThat(List.of(response.getBody())).isNotEmpty();
    }
}

