package com.learnings.auth_service.controller;

import com.learnings.auth_service.dto.LoginResponse;
import com.learnings.auth_service.dto.LoginValidation;
import com.learnings.auth_service.dto.RegisterValidation;
import com.learnings.auth_service.dto.UserDTO;
import com.learnings.auth_service.exceptions.UserAlreadyExistsException;
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

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<Void> register(@RequestBody @Validated(RegisterValidation.class) UserDTO userDTO) throws UserAlreadyExistsException {
        userService.registerUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Validated(LoginValidation.class) UserDTO userDTO) {
        return ResponseEntity.ok(userService.authenticate(userDTO));
    }
}
