package com.example.demosecurity.services;

import com.example.demosecurity.dtos.request.GameRequestDto;
import com.example.demosecurity.dtos.response.GameResponseDto;
import com.example.demosecurity.models.Game;

import java.util.List;

public interface GameService {

    GameResponseDto create(GameRequestDto request);

    List<GameResponseDto> getAll(Boolean active);

    GameResponseDto getById(Long gameId);

    Game getEntityById(Long gameId);

    void updateStock(Long gameId, Integer stock);
}
