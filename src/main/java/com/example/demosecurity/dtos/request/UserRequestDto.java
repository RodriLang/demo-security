package com.example.demosecurity.dtos.request;

import com.example.demosecurity.enums.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UserRequestDto(

        @NotBlank
        String username,

        @NotBlank
        @Size(min = 8, max = 12)
        String password,

        Set<RoleType> roles
) {
}
