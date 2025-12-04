package com.edu.api_supermercado.dtos.sucursal;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SucursalRequest(
    @NotEmpty @Size(min = 1, max = 50) String nombre,
    @NotEmpty @Size(min = 1, max = 150)String direccion,
    @NotEmpty @Size(min = 9, max = 9)String telefono
) {
}
