package com.example.demosecurity.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(

        @NotBlank
        String username,

        @NotBlank
        String password
) {
}
