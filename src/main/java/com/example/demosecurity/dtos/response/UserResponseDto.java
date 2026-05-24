package com.example.demosecurity.dtos.response;

import com.example.demosecurity.enums.RoleType;

public record UserResponseDto(

        Long id,

        String username,

        RoleType role
) {
}
