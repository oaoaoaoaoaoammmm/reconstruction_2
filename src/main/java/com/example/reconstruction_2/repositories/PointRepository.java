package com.example.reconstruction_2.repositories;

import com.example.reconstruction_2.models.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PointRepository extends JpaRepository<Point, Long> {
    Collection<Point> findAllByUserUsername(String username);
    void deleteAllByUserUsername(String username);
}
