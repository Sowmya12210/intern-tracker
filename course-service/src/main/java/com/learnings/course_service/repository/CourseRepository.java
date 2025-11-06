package com.learnings.course_service.repository;

import com.learnings.course_service.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(
            value = "SELECT c.* FROM courses c " +
                    "JOIN course_enrollment ce ON c.id = ce.course_id " +
                    "WHERE ce.intern_id = :internId",
            nativeQuery = true
    )
    List<Course> findCoursesByInternId(@Param("internId") Long internId);
}
