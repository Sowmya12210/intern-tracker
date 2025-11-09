package com.learnings.auth_service.dto;

import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

public record ErrorDetails(String message, LocalDateTime time, HttpStatus status) {
}
