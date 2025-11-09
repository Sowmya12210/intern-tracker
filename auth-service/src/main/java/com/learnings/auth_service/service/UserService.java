package com.learnings.auth_service.service;

import com.learnings.auth_service.dto.LoginResponse;
import com.learnings.auth_service.dto.UserDTO;
import com.learnings.auth_service.entity.Role;
import com.learnings.auth_service.entity.User;
import com.learnings.auth_service.enums.RoleName;
import com.learnings.auth_service.exceptions.UserAlreadyExistsException;
import com.learnings.auth_service.mapper.UserMapper;
import com.learnings.auth_service.repository.RoleRepository;
import com.learnings.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    // authentication manager delegates the authentication process to the provider(s)
    private final AuthenticationManager authenticationManager;

    public UserDTO registerUser(UserDTO userDTO) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = fetchRoles(userDTO.getRoles());
        user.setRoles(roles);
        return userMapper.toDTO(userRepository.save(user));
    }

    private Set<Role> fetchRoles(Set<RoleName> roleNames) {
        return roleNames.stream()
                .map(roleName -> roleRepository.findByRoleName(roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName)))
                .collect(Collectors.toSet());
    }

    public LoginResponse authenticate(UserDTO userDTO) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));
        if (auth.isAuthenticated()) {
            User user = userRepository.findByUsername(userDTO.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return new LoginResponse(jwtService.generateToken(user));
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }
}
