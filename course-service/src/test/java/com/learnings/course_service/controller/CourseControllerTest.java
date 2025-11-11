package com.learnings.course_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnings.course_service.dto.CourseDTO;
import com.learnings.course_service.service.CourseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllCourses_returnsList() throws Exception {
        CourseDTO dto = new CourseDTO(1L, "Java Essentials");
        Mockito.when(courseService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Java Essentials"));
    }

    @Test
    void getCourseById_returnsCourse() throws Exception {
        CourseDTO dto = new CourseDTO(2L, "Spring Security");
        Mockito.when(courseService.findById(2L)).thenReturn(dto);

        mockMvc.perform(get("/api/courses/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Spring Security"));
    }

    @Test
    void postCourse_savesAndReturnsCourse() throws Exception {
        CourseDTO dto = new CourseDTO(3L, "React Basics");
        Mockito.when(courseService.save(any(CourseDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    void findMyCourses_returnsList() throws Exception {
        CourseDTO dto = new CourseDTO(4L, "Microservices 101");
        Mockito.when(courseService.findMyCourses(100L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/courses/mine")
                        .header("X-User-Id", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(4));
    }
}

