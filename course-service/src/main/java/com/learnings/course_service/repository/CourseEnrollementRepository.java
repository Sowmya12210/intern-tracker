package com.learnings.course_service.repository;

import com.learnings.course_service.entity.CourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseEnrollementRepository extends JpaRepository<CourseEnrollment, Long> {
}
