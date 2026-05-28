package com.example.demosecurity.controllers;

import com.example.demosecurity.dtos.request.GameRequestDto;
import com.example.demosecurity.dtos.response.GameResponseDto;
import com.example.demosecurity.services.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<GameResponseDto> create(@RequestBody @Valid GameRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.create(request));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<GameResponseDto>> getAll(@RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(gameService.getAll(active));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getById(id));
    }
}

