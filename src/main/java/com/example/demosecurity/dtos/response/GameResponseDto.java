package com.example.demosecurity.dtos.response;

import lombok.*;

@Builder
public record GameResponseDto (
        Long id,

        String name,

        String category,

        Integer minAge,

        Integer availableStock
){
}
