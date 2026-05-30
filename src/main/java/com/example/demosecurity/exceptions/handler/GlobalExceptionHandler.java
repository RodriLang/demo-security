package com.example.demosecurity.exceptions.handler;

import com.example.demosecurity.exceptions.*;
import com.example.demosecurity.exceptions.response.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {

        ErrorResponseDto response = buidResponse(
                HttpStatus.BAD_REQUEST,
                "Error de validación de los datos enviados",
                request.getServletPath()
        );

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        response.setValidations(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<ErrorResponseDto> handleGameNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        ErrorResponseDto response = buidResponse(HttpStatus.NOT_FOUND, e.getMessage(), request.getServletPath());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InactiveGameException.class)
    private ResponseEntity<ErrorResponseDto> handleInactiveGameException(InactiveGameException e, HttpServletRequest request) {
        ErrorResponseDto response = buidResponse(HttpStatus.CONFLICT, e.getMessage(), request.getServletPath());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentials(BadCredentialsException e, HttpServletRequest request) {
        ErrorResponseDto response = buidResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), request.getServletPath());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUsernameNotFound(UsernameNotFoundException e, HttpServletRequest request) {
        ErrorResponseDto response = buidResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), request.getServletPath());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(DuplicatedEntityException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicatedEntity(DuplicatedEntityException e, HttpServletRequest request) {
        ErrorResponseDto response = buidResponse(HttpStatus.CONFLICT, e.getMessage(), request.getServletPath());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e, HttpServletRequest request) {
        ErrorResponseDto response = buidResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request.getServletPath());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private ErrorResponseDto buildResponse(HttpStatus status, String message, String path) {
        return ErrorResponseDto.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
