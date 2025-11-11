package com.learnings.intern_service.controller;

import com.learnings.intern_service.entity.Feedback;
import com.learnings.intern_service.entity.Intern;
import com.learnings.intern_service.service.InternService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/interns")
public class InternController {
    private final InternService internService;

    @GetMapping
    public List<Intern> findAllInterns(){
        return internService.findAllInterns();
    }

    @PostMapping
    public Intern saveIntern(@RequestBody Intern intern){
        return internService.saveIntern(intern);
    }

    @GetMapping("/{id}")
    public Intern findInternById(@PathVariable Long id) throws Exception {
        return internService.findInternById(id);
    }

    @PatchMapping("/{id}/assign-mentor/{mentorId}")
    public Intern assignMentor(@PathVariable Long id, @PathVariable Long mentorId) throws Exception {
        return internService.assignMentor(id,mentorId);
    }

    @GetMapping("/me")
    public List<Intern> findMyInterns(@RequestHeader("X-User-Id") Long mentorId) {
        return internService.findInternsByMentorId(mentorId);
    }

    @PostMapping("/feedback/{internId}")
    public Feedback postFeedback(@RequestHeader("X-User-Id") Long mentorId, @RequestHeader("X-User-Username") String username, @PathVariable Long internId, @RequestBody Feedback feedback) throws Exception {
        return internService.saveFeedback(internId, username, mentorId, feedback);
    }
}
