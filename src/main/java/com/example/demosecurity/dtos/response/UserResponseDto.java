package com.example.demosecurity.dtos.response;

import java.util.Set;

public record UserResponseDto(

        Long id,

        String username,

        Set<RoleResponseDto> roles
) {
}
