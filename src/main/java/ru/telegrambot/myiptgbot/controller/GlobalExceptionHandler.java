package ru.telegrambot.myiptgbot.controller;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.telegrambot.myiptgbot.service.IpClientException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IpClientException.class)
    public ResponseEntity<String> handleIpClientException(IpClientException e) {
        log.error("IP service error", e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Failed to retrieve IP: " + e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException e) {
        log.warn("Validation error", e);
        return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        log.error("Unexpected error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error");
    }
}
