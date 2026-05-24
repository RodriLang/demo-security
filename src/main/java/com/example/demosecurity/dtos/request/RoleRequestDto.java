package com.example.demosecurity.dtos.request;

import com.example.demosecurity.enums.RoleType;
import jakarta.validation.constraints.NotNull;

public record RoleRequestDto(

        @NotNull
        RoleType name
) {
}