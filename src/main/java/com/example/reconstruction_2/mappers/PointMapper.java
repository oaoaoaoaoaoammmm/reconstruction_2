package com.example.reconstruction_2.mappers;

import com.example.reconstruction_2.dtos.RespPointDto;
import com.example.reconstruction_2.models.Point;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PointMapper {
    RespPointDto convert(Point point);
}
