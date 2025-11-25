package com.edu.api_supermercado.db;

import com.edu.api_supermercado.entity.Producto;
import com.edu.api_supermercado.enums.CategoriaProducto;

import java.math.BigDecimal;

public class DBTest {

    public static Producto entity() {
        return Producto.builder()
                .id(1L)
                .nombre("Coca Cola")
                .categoria(CategoriaProducto.BEBIDAS)
                .precio(BigDecimal.valueOf(3.5))
                .stock(10)
                .isDeleted(false)
                .build();
    }
}
