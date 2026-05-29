package com.example.demosecurity.dtos.response;

import lombok.Builder;

@Builder
public record LoginResponseDto(
        String token
) {
}
