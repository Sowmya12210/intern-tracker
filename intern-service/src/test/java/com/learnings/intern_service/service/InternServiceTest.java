package com.learnings.intern_service.service;

import com.learnings.intern_service.entity.Feedback;
import com.learnings.intern_service.entity.Intern;
import com.learnings.intern_service.event.FeedbackService;
import com.learnings.intern_service.repository.InternRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InternServiceTest {

    @Mock
    private InternRepository internRepository;

    @Mock
    private FeedbackService feedbackService;

    @InjectMocks
    private InternService internService;

    @Captor
    ArgumentCaptor<Intern> internCaptor;

    private Intern sampleIntern;

    @BeforeEach
    void setup() {
        sampleIntern = new Intern();
        sampleIntern.setId(1L);
        sampleIntern.setMentorUserIds(new HashSet<>(Arrays.asList(100L)));
    }

    @Test
    void findAllInterns_returnsList() {
        when(internRepository.findAll()).thenReturn(List.of(sampleIntern));
        var result = internService.findAllInterns();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(internRepository).findAll();
    }

    @Test
    void findInternsByMentorId_delegatesToRepo() {
        when(internRepository.findAllByMentorUserIdsContaining(100L)).thenReturn(List.of(sampleIntern));
        var result = internService.findInternsByMentorId(100L);
        assertEquals(1, result.size());
        verify(internRepository).findAllByMentorUserIdsContaining(100L);
    }

    @Test
    void assignMentor_addsMentorAndSaves() throws Exception {
        when(internRepository.findById(1L)).thenReturn(Optional.of(sampleIntern));
        when(internRepository.save(any(Intern.class))).thenAnswer(inv -> inv.getArgument(0));

        var updated = internService.assignMentor(1L, 200L);
        assertTrue(updated.getMentorUserIds().contains(200L));
        verify(internRepository).save(internCaptor.capture());
        assertTrue(internCaptor.getValue().getMentorUserIds().contains(200L));
    }

    @Test
    void assignMentor_throwsIfInternMissing() {
        when(internRepository.findById(1L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> internService.assignMentor(1L, 200L));
        assertEquals("Invalid intern id", ex.getMessage());
    }

    @Test
    void findInternById_success() throws Exception {
        when(internRepository.findById(1L)).thenReturn(Optional.of(sampleIntern));
        Intern found = internService.findInternById(1L);
        assertEquals(1L, found.getId());
    }

    @Test
    void findInternById_notFound_throws() {
        when(internRepository.findById(1L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> internService.findInternById(1L));
        assertEquals("Intern not found", ex.getMessage());
    }

    @Test
    void saveFeedback_delegatesToFeedbackService() throws Exception {
        Feedback feedback = new Feedback();
        when(feedbackService.createFeedback(any(Feedback.class))).thenAnswer(inv -> {
            Feedback f = inv.getArgument(0);
            f.setId(10L);
            return f;
        });

        Feedback saved = internService.saveFeedback(5L, "mentorName", 42L, feedback);
        assertEquals(5L, saved.getInternId());
        assertEquals("mentorName", saved.getMentorName());
        assertEquals(42L, saved.getMentorId());
        verify(feedbackService).createFeedback(any(Feedback.class));
    }

    @Test
    void saveIntern_callsRepositorySave() {
        when(internRepository.save(sampleIntern)).thenReturn(sampleIntern);
        Intern result = internService.saveIntern(sampleIntern);
        verify(internRepository).save(sampleIntern);
        assertSame(sampleIntern, result);
    }
}

