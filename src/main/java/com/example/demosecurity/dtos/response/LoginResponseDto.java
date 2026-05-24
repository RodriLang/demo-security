package com.example.demosecurity.dtos.response;

import lombok.Builder;

import java.util.List;

@Builder
public record LoginResponseDto(
        String username,
        List<String> roles,
        String message
) {
}
