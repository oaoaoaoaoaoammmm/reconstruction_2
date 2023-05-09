package com.example.reconstruction_2.controllers;

import com.example.reconstruction_2.dtos.ReqUserDto;
import com.example.reconstruction_2.dtos.TokenDto;
import com.example.reconstruction_2.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register user")
    @ApiResponse(responseCode = "201", description = "User registered")
    @ApiResponse(responseCode = "400", description = "Data is invalid")
    @ApiResponse(responseCode = "403", description = "You have not enough rights")
    @ApiResponse(responseCode = "409", description = "There is user")
    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(
            @RequestBody @Valid ReqUserDto userDto
    ) {
        userService.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Authenticate user")
    @ApiResponse(responseCode = "200", description = "User authenticated")
    @ApiResponse(responseCode = "400", description = "Data is invalid")
    @ApiResponse(responseCode = "401", description = "Wrong data for user")
    @ApiResponse(responseCode = "403", description = "You have not enough rights")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PostMapping("/authorization")
    public ResponseEntity<?> authenticateUser(
            @RequestBody @Valid ReqUserDto userDto
    ) {
        TokenDto tokenDto = userService.authenticateUser(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(tokenDto);
    }
}
