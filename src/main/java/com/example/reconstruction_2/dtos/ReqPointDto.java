package com.example.reconstruction_2.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReqPointDto(
        @NotNull @Min(-5) @Max(3) double x,
        @NotNull @Min(-5) @Max(5) double y,
        @NotNull @Min(1) @Max(5) double r) { }
