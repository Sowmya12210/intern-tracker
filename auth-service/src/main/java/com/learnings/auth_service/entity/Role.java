package com.learnings.auth_service.entity;

import com.learnings.auth_service.enums.RoleName;
import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;
}
