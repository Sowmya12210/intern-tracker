package com.learnings.intern_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "interns")
@Entity
@Getter
@Setter
public class Intern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> mentorUserIds;
    private String displayName;
    @CreationTimestamp
    private LocalDateTime joinedDate;
}
