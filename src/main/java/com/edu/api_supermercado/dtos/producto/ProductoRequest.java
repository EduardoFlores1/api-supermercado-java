package com.edu.api_supermercado.dtos.producto;

import com.edu.api_supermercado.enums.CategoriaProducto;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductoRequest(
        @NotEmpty String nombre,
        @NotNull CategoriaProducto categoria,
        @NotNull @Digits(integer = 10, fraction = 2) BigDecimal precio,
        @NotNull @Size(min = 1) Integer stock
) {
}
