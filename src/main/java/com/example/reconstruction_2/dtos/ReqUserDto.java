package com.example.reconstruction_2.dtos;

import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

public record ReqUserDto(
        @NonNull @Length(min = 3, max = 20) String username,
        @NonNull @Length(min = 5) String password) { }
