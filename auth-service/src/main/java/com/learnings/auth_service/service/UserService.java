package com.learnings.auth_service.service;

import com.learnings.auth_service.dto.LoginResponse;
import com.learnings.auth_service.dto.UserDTO;
import com.learnings.auth_service.entity.User;
import com.learnings.auth_service.exceptions.UserAlreadyExistsException;
import com.learnings.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    // authentication manager delegates the authentication process to the provider(s)
    private final AuthenticationManager authenticationManager;

    public User registerUser(User user) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public LoginResponse authenticate(UserDTO userDTO) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));
        if (auth.isAuthenticated()) {
            User user = userRepository.findByUsername(userDTO.getUsername()).get();
            return new LoginResponse(jwtService.generateToken(user));
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }

}
