package com.edu.api_supermercado.dtos.sucursal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class SucursalResponse {
    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
}
