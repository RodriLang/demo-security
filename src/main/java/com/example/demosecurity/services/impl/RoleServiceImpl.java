package com.example.demosecurity.services.impl;

import com.example.demosecurity.dtos.request.RoleRequestDto;
import com.example.demosecurity.dtos.response.RoleResponseDto;
import com.example.demosecurity.enums.RoleType;
import com.example.demosecurity.exceptions.DuplicatedEntityException;
import com.example.demosecurity.exceptions.EntityNotFoundException;
import com.example.demosecurity.mappers.RoleMapper;
import com.example.demosecurity.models.RoleEntity;
import com.example.demosecurity.repositories.RoleRepository;
import com.example.demosecurity.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleEntity findByName(RoleType roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role no encontrado"));
    }

    @Override
    public List<RoleResponseDto> findAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toDto)
                .toList();
    }

    @Override
    public RoleResponseDto findById(Long id) {
        RoleEntity roleEntity = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role no encontrado"));

        return roleMapper.toDto(roleEntity);
    }

    @Override
    public RoleResponseDto create(RoleRequestDto request) {

        if (roleRepository.existsByRoleName(request.name())) {
            throw new DuplicatedEntityException("El rol ya existe");
        }

        RoleEntity roleEntity = RoleEntity.builder()
                .roleName(request.name())
                .build();

        RoleEntity savedRoleEntity = roleRepository.save(roleEntity);

        return roleMapper.toDto(savedRoleEntity);
    }
}