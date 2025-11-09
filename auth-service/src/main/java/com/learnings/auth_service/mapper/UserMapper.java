package com.learnings.auth_service.mapper;

import com.learnings.auth_service.dto.UserDTO;
import com.learnings.auth_service.entity.Role;
import com.learnings.auth_service.entity.User;
import com.learnings.auth_service.enums.RoleName;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToRoleNames")
    UserDTO toDTO(User user);

    @Named("rolesToRoleNames")
    default Set<RoleName> rolesToRoleNames(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return new HashSet<>();
        }
        return roles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());
    }

    @Mapping(source = "roles", target = "roles", qualifiedByName = "roleNamesToRoles")
    User toEntity(UserDTO userDTO);

    @Named("roleNamesToRoles")
    default Set<Role> roleNamesToRoles(Set<RoleName> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return new HashSet<>();
        }
        return roleNames.stream()
                .map(roleName -> {
                    Role role = new Role();
                    role.setRoleName(roleName);
                    return role;
                })
                .collect(Collectors.toSet());
    }
}

