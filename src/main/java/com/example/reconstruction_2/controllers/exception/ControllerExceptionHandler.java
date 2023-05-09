package com.example.reconstruction_2.controllers.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            NoSuchElementException.class
    })
    public ResponseEntity<?> notFound(Exception ex) {
        ErrorMessage errorMessage = getMessage(ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorMessage);
    }

    @ExceptionHandler(value = AuthenticationServiceException.class)
    public ResponseEntity<?> authFailed(Exception ex) {
        ErrorMessage errorMessage = getMessage(ex);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorMessage);
    }

    @ExceptionHandler(value = IllegalCallerException.class)
    public ResponseEntity<?> regFailed(Exception ex) {
        ErrorMessage errorMessage = getMessage(ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorMessage);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> invalidData(Exception ex) {
        ErrorMessage errorMessage = getMessage(ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);
    }

    private ErrorMessage getMessage(Exception ex) {
        return ErrorMessage.builder()
                .message(ex.getMessage())
                .time(LocalDateTime.now())
                .build();
    }
}
