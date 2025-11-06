package com.learnings.course_service.entity;

import com.learnings.course_service.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CourseEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @Column(name = "intern_id", nullable = false)
    private Long internId;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status = EnrollmentStatus.ENROLLED;

    @CreationTimestamp
    private LocalDateTime enrolledAt;

    public CourseEnrollment(Long internId, Course course) {
        this.internId = internId;
        this.course = course;
    }
}
