package com.learnings.course_service.integration_test;

import com.learnings.course_service.entity.Course;
import com.learnings.course_service.entity.CourseEnrollment;
import com.learnings.course_service.repository.CourseEnrollementRepository;
import com.learnings.course_service.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourseEnrollmentIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseEnrollementRepository enrollmentRepository;

    @Test
    void enrollCourse_persistsEnrollment() {
        Course course = courseRepository.save(new Course(1L, "Mentor Connect 101","",1,""));

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", "123");

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<CourseEnrollment> response =
                restTemplate.postForEntity("/api/courses/" + course.getId() + "/enroll", request, CourseEnrollment.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getInternId()).isEqualTo(123L);

        assertThat(enrollmentRepository.findAll()).hasSize(1);
    }
}
