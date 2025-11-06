package com.learnings.auth_service.controller;

import com.learnings.auth_service.dto.LoginResponse;
import com.learnings.auth_service.dto.LoginValidation;
import com.learnings.auth_service.dto.RegisterValidation;
import com.learnings.auth_service.dto.UserDTO;
import com.learnings.auth_service.entity.User;
import com.learnings.auth_service.exceptions.UserAlreadyExistsException;
import com.learnings.auth_service.mapper.UserMapper;
import com.learnings.auth_service.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> register(@RequestBody @Validated(RegisterValidation.class) UserDTO userDTO) throws UserAlreadyExistsException {
        User user = userMapper.toEntity(userDTO);
        userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Validated(LoginValidation.class) UserDTO userDTO) {
        return ResponseEntity.ok(userService.authenticate(userDTO));
    }
}
