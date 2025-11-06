package com.learnings.intern_service.repository;

import com.learnings.intern_service.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Long> {
    List<Feedback> findAllByInternId(Long internId);
    List<Feedback> findAllByMentorId(Long userId);
}
