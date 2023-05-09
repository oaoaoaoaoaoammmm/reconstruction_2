package com.example.reconstruction_2.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RespPointDto {
    private double x;
    private double y;
    private double r;
    private boolean hit;
    private LocalDateTime time;
}
