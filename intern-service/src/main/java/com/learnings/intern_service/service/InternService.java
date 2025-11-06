package com.learnings.intern_service.service;

import com.learnings.intern_service.entity.Feedback;
import com.learnings.intern_service.entity.Intern;
import com.learnings.intern_service.event.FeedbackService;
import com.learnings.intern_service.repository.InternRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InternService {
    private final InternRepository internRepository;
    private final FeedbackService feedbackService;

    public List<Intern> findAllInterns(){
        return internRepository.findAll();
    }

    public List<Intern> findInternsByMentorId(Long mentorId){
        return internRepository.findAllByMentorUserIdsContaining(mentorId);
    }

    public Intern assignMentor(Long internId, Long mentorId) throws Exception {
        Intern intern = internRepository.findById(internId).orElse(null);
        if(intern == null){
            throw new Exception("Invalid intern id");
        }
        Set<Long> mentorIds = intern.getMentorUserIds();
        if(mentorIds == null){
            mentorIds = new HashSet<>();
        }
        mentorIds.add(mentorId);
        intern.setMentorUserIds(mentorIds);
        return internRepository.save(intern);
    }

    public Intern findInternById(Long id) throws Exception {
        Optional<Intern> intern = internRepository.findById(id);
        if(intern.isPresent()){
            return intern.get();
        } else {
            throw new Exception("Intern not found");
        }
    }

    public Feedback saveFeedback(Long internId, String username, Long userId, Feedback feedback) throws Exception {
        feedback.setInternId(internId);
        feedback.setMentorName(username);
        feedback.setMentorId(userId);
        return feedbackService.createFeedback(feedback);
    }

    public Intern saveIntern(Intern intern){
        return internRepository.save(intern);
    }
}
