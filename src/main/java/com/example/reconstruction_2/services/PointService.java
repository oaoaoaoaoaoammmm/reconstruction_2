package com.example.reconstruction_2.services;

import com.example.reconstruction_2.dtos.ReqPointDto;
import com.example.reconstruction_2.dtos.RespPointDto;
import com.example.reconstruction_2.mappers.PointMapper;
import com.example.reconstruction_2.models.Point;
import com.example.reconstruction_2.repositories.PointRepository;
import com.example.reconstruction_2.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PointService {

    private final PointRepository pointRepo;
    private final UserRepository userRepo;
    private final PointMapper pointMapper;

    public PointService(
            PointRepository pointRepo,
            UserRepository userRepo,
            PointMapper pointMapper
    ) {
        this.pointRepo = pointRepo;
        this.userRepo = userRepo;
        this.pointMapper = pointMapper;
    }

    public CompletableFuture<Collection<RespPointDto>> findAllByUsername(String username) {
        log.debug("Finding all points for - {}", username);
        return CompletableFuture.supplyAsync(() -> pointRepo.findAllByUserUsername(username))
                .thenApply(this::mapPointsToDto);
    }

    public CompletableFuture<RespPointDto> addPointForUser(ReqPointDto pointDto, String username) {
        log.debug("Adding point for {}", username);
        return CompletableFuture.supplyAsync(() -> userRepo.findByUsername(username))
                .thenCompose(user -> {
                    Point point = createPoint(pointDto.x(), pointDto.y(), pointDto.r());
                    point.setUser(user.orElseThrow(() -> {
                        throw new NoSuchElementException("User not found");
                    }));
                    return CompletableFuture.supplyAsync(() -> pointRepo.save(point));
                })
                .thenApply(pointMapper::convert);
    }

    @Transactional
    public void deleteAllByUsername(String username) {
        log.debug("Deleting all point for - {}", username);
        pointRepo.deleteAllByUserUsername(username);
    }

    private Point createPoint(double x, double y, double r) {
        log.trace("Creating point x - {}, y {}, r {}", x, y, r);
        return Point.builder()
                .x(x)
                .y(y)
                .r(r)
                .hit(checkHit(x, y, r))
                .time(LocalDateTime.now())
                .build();
    }

    private boolean checkHit(double x, double y, double r) {
        log.trace("Checking hit x - {}, y {}, r {}", x, y, r);
        if ((x >= 0) && (x <= r) && (y <= r) && (y >= 0)) {
            return true;
        } else if ((x <= 0) && (y <= 0) && (-y <= x + r)) {
            return true;
        } else return (x >= 0) && (y <= 0) && (x * x + y * y <= (r / 2) * (r / 2));
    }

    private List<RespPointDto> mapPointsToDto(Collection<Point> collection) {
        return collection.stream()
                .map(pointMapper::convert)
                .collect(Collectors.toList());
    }
}
