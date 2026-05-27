package com.example.demosecurity.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequestDto(

        @NotBlank
        String refreshToken
) {
}
