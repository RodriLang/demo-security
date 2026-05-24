package com.example.demosecurity.mappers;

import com.example.demosecurity.dtos.request.GameRequestDto;
import com.example.demosecurity.dtos.response.GameResponseDto;
import com.example.demosecurity.models.Game;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameMapper {

    Game toEntity (GameRequestDto dto);

    GameResponseDto toDto(Game entity);
}
