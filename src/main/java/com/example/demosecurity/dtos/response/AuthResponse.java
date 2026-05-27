package com.example.demosecurity.dtos.response;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
