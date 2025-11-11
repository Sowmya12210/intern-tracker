package com.learnings.intern_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnings.intern_service.entity.Feedback;
import com.learnings.intern_service.entity.Intern;
import com.learnings.intern_service.service.InternService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;

@WebMvcTest(InternController.class)
public class InternControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InternService internService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllInterns_returnsJsonList() throws Exception {
        Intern i = new Intern();
        i.setId(1L);
        Mockito.when(internService.findAllInterns()).thenReturn(List.of(i));

        mockMvc.perform(get("/api/interns"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void saveIntern_returnsSavedIntern() throws Exception {
        Intern i = new Intern();
        i.setId(2L);
        Mockito.when(internService.saveIntern(any(Intern.class))).thenReturn(i);

        mockMvc.perform(post("/api/interns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Intern())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void getInternById_returnsIntern() throws Exception {
        Intern i = new Intern();
        i.setId(3L);
        Mockito.when(internService.findInternById(3L)).thenReturn(i);

        mockMvc.perform(get("/api/interns/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    void assignMentor_endpoint() throws Exception {
        Intern i = new Intern();
        i.setId(4L);
        i.setMentorUserIds(Set.of(10L, 20L));
        Mockito.when(internService.assignMentor(4L, 30L)).thenReturn(i);

        mockMvc.perform(patch("/api/interns/4/assign-mentor/30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4));
    }

    @Test
    void postFeedback_endpoint() throws Exception {
        Feedback f = new Feedback();
        f.setId(99L);
        Mockito.when(internService.saveFeedback(eq(5L), anyString(), eq(11L), any(Feedback.class))).thenReturn(f);

        mockMvc.perform(post("/api/interns/feedback/5")
                        .header("X-User-Id", "11")
                        .header("X-User-Username", "mentorUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Feedback())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(99));
    }
}


