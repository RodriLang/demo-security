package com.example.demosecurity.dtos.response;

import com.example.demosecurity.enums.RoleType;

public record RoleResponseDto(
        Long id,
        RoleType name
) {
}