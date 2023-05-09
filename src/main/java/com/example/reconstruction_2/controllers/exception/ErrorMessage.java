package com.example.reconstruction_2.controllers.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorMessage {
    private String message;
    private LocalDateTime time;
}
