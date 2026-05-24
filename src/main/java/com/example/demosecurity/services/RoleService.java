package com.example.demosecurity.services;

import com.example.demosecurity.dtos.request.RoleRequestDto;
import com.example.demosecurity.dtos.response.RoleResponseDto;

import java.util.List;

public interface RoleService {

    List<RoleResponseDto> findAll();

    RoleResponseDto findById(Long id);

    RoleResponseDto create(RoleRequestDto request);

}