package com.example.demosecurity.services;

import com.example.demosecurity.dtos.request.RoleRequestDto;
import com.example.demosecurity.dtos.response.RoleResponseDto;
import com.example.demosecurity.enums.RoleType;
import com.example.demosecurity.models.RoleEntity;

import java.util.List;

public interface RoleService {

    RoleEntity findByName(RoleType roleName);

    List<RoleResponseDto> findAll();

    RoleResponseDto findById(Long id);

    RoleResponseDto create(RoleRequestDto request);

}