package com.learnings.auth_service.mapper;

import com.learnings.auth_service.dto.UserDTO;
import com.learnings.auth_service.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);
}

