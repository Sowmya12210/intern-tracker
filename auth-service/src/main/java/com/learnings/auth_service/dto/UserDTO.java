package com.learnings.auth_service.dto;

import com.learnings.auth_service.enums.RoleName;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserDTO {
    private Long id;

    @Size(min = 3, max = 50, message = "Username should be greater than 3 characters", groups = {RegisterValidation.class, LoginValidation.class})
    private String username;

    @Size(min = 5, max = 50, message = "Password should be greater than 5 characters", groups = {RegisterValidation.class, LoginValidation.class})
    private String password;

    @NotEmpty(message = "User must have at least one role",  groups = {RegisterValidation.class})
    private Set<RoleName> roles;
    private LocalDateTime createdDate;
}
