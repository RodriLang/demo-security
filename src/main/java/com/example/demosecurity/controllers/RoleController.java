package com.example.demosecurity.controllers;

import com.example.demosecurity.dtos.request.RoleRequestDto;
import com.example.demosecurity.dtos.response.RoleResponseDto;
import com.example.demosecurity.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<RoleResponseDto> findAll() {
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    public RoleResponseDto findById(@PathVariable Long id) {
        return roleService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleResponseDto create(@Valid @RequestBody RoleRequestDto request) {
        return roleService.create(request);
    }
}