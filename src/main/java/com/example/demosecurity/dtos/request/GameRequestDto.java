package com.example.demosecurity.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record GameRequestDto (

    @NotBlank(message = "Se debe ingresar un nombre")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
     String name,

    @NotBlank(message = "Se debe ingresar una categoria")
    @Size(max = 100, message = "La categoria debe tener menos de 100 caracteres")
     String category,

    @Positive(message = "El numero debe ser mayor a cero")
     Integer minAge,

    @Positive(message = "El stock no puede ser negativo")
     Integer availableStock
){
}
