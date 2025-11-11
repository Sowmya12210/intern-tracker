package com.learnings.course_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnings.course_service.entity.CourseEnrollment;
import com.learnings.course_service.service.CourseEnrollementService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseEnrollementController.class)
class CourseEnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseEnrollementService courseEnrollementService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void enrollCourse_returnsEnrollment() throws Exception {
        CourseEnrollment enrollment = new CourseEnrollment(42L, null);
        enrollment.setId(10L);

        Mockito.when(courseEnrollementService.enrollCourse(42L, 5L)).thenReturn(enrollment);

        mockMvc.perform(post("/api/courses/5/enroll")
                        .header("X-User-Id", "42")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }
}

