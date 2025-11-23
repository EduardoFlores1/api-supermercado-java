package com.edu.api_supermercado.dtos.producto;

import com.edu.api_supermercado.enums.CategoriaProducto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductoResponse{
    private Long id;
    private String nombre;
    private CategoriaProducto categoria;
    private BigDecimal precio;
    private Integer stock;
    private Boolean isDeleted;
}
