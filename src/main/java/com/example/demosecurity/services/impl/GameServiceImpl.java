package com.example.demosecurity.services.impl;

import com.example.demosecurity.dtos.request.GameRequestDto;
import com.example.demosecurity.dtos.response.GameResponseDto;
import com.example.demosecurity.exceptions.EntityNotFoundException;
import com.example.demosecurity.mappers.GameMapper;
import com.example.demosecurity.models.Game;
import com.example.demosecurity.repositories.GameRepository;
import com.example.demosecurity.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    @Override
    public GameResponseDto create(GameRequestDto request) {

        Game newGame = gameMapper.toEntity(request);

        Game savedGame = gameRepository.save(newGame);

        return gameMapper.toDto(savedGame);
    }

    @Override
    public List<GameResponseDto> getAll(Boolean active) {
        List<Game> games;

        if (Objects.isNull(active)) {
            games = gameRepository.findAll();
        } else {
            games = gameRepository.findByActive(active);
        }

        return games.stream()
                .map(gameMapper::toDto)
                .toList();
    }

    @Override
    public GameResponseDto getById(Long gameId) {
        Game game = getEntityById(gameId);
        return gameMapper.toDto(game);
    }

    @Override
    public Game getEntityById(Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Juego no encontrado con el ID = " + gameId));
    }

    @Override
    public void updateStock(Long gameId, Integer stock) {
        Game game = getEntityById(gameId);

        game.setAvailableStock(stock);

        gameRepository.save(game);
    }
}
