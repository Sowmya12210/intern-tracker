package com.learnings.intern_service.repository;

import com.learnings.intern_service.entity.Intern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternRepository extends JpaRepository<Intern,Long> {

    @Query("SELECT i FROM Intern i JOIN i.mentorUserIds m WHERE m = :mentorUserId")
    List<Intern> findAllByMentorUserIdsContaining(@Param("mentorUserId") Long mentorUserId);
}
