package com.example.reconstruction_2.controllers;

import com.example.reconstruction_2.dtos.ReqPointDto;
import com.example.reconstruction_2.dtos.RespPointDto;
import com.example.reconstruction_2.services.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/points")
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @Operation(summary = "Find all points for user")
    @ApiResponse(responseCode = "200", description = "Found all")
    @ApiResponse(responseCode = "403", description = "You have not enough rights")
    @ApiResponse(responseCode = "404", description = "Not found user by username")
    @GetMapping("/{username}")
    public CompletableFuture<ResponseEntity<Collection<RespPointDto>>> findAllByUsername(
            @PathVariable String username
    ) {
        return pointService.findAllByUsername(username)
                .exceptionally(exception -> {
                    log.warn("Error find all points for user - {}", username, exception);
                    throw new IllegalArgumentException("Can't find points by username", exception);
                })
                .thenApply(list -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(list)
                );
    }

    @Operation(summary = "Add point for user")
    @ApiResponse(responseCode = "201", description = "Point added")
    @ApiResponse(responseCode = "400", description = "Data is invalid")
    @ApiResponse(responseCode = "403", description = "You have not enough rights")
    @ApiResponse(responseCode = "404", description = "Not found user by username")
    @PostMapping("/{username}")
    public CompletableFuture<ResponseEntity<RespPointDto>> addPointForUser(
            @PathVariable String username,
            @RequestBody @Valid ReqPointDto reqPointDto
    ) {
        return pointService.addPointForUser(reqPointDto, username)
                .exceptionally(exception -> {
                    log.warn("Error add point for user - {}", username, exception);
                    throw new IllegalArgumentException("Can't add point", exception);
                })
                .thenApply((respPointDto) -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(respPointDto)
                );
    }

    @Operation(summary = "Delete all points for user")
    @ApiResponse(responseCode = "200", description = "Points deleted")
    @ApiResponse(responseCode = "403", description = "You have not enough rights")
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteAllByUsername(
            @PathVariable String username
    ) {
        pointService.deleteAllByUsername(username);
        return ResponseEntity.noContent().build();
    }
}
