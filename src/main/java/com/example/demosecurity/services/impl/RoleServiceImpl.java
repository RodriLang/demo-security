package com.example.demosecurity.services.impl;

import com.example.demosecurity.dtos.request.RoleRequestDto;
import com.example.demosecurity.dtos.response.RoleResponseDto;
import com.example.demosecurity.exceptions.DuplicatedEntityException;
import com.example.demosecurity.exceptions.EntityNotFoundException;
import com.example.demosecurity.models.Role;
import com.example.demosecurity.repositories.RoleRepository;
import com.example.demosecurity.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<RoleResponseDto> findAll() {
        return roleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public RoleResponseDto findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));

        return toResponse(role);
    }

    @Override
    public RoleResponseDto create(RoleRequestDto request) {

        if (roleRepository.existsByName(request.name())) {
            throw new DuplicatedEntityException("El rol ya existe");
        }

        Role role = Role.builder()
                .name(request.name())
                .build();

        Role savedRole = roleRepository.save(role);

        return toResponse(savedRole);
    }

    private RoleResponseDto toResponse(Role role) {
        return new RoleResponseDto(
                role.getId(),
                role.getName()
        );
    }
}