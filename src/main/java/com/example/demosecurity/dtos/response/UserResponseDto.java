package com.example.demosecurity.dtos.response;

import java.util.List;

public record UserResponseDto(

        Long id,

        String username,

        List<RoleResponseDto> roles
) {
}
