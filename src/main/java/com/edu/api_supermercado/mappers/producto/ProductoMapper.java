package com.edu.api_supermercado.mappers.producto;

import com.edu.api_supermercado.dtos.producto.ProductoRequest;
import com.edu.api_supermercado.dtos.producto.ProductoResponse;
import com.edu.api_supermercado.entity.Producto;

public class ProductoMapper {

    public static ProductoResponse toListResponse(Producto p) {

        if(p == null) return null;

        return ProductoResponse.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .categoria(p.getCategoria())
                .precio(p.getPrecio())
                .stock(p.getStock())
                .build();
    }

    public static ProductoResponse toFindByIdResponse(Producto p) {

        if(p == null) return null;

        return ProductoResponse.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .categoria(p.getCategoria())
                .precio(p.getPrecio())
                .stock(p.getStock())
                .isDeleted(p.getIsDeleted())
                .build();
    }

    public static Producto toCreateEntity(ProductoRequest r) {

        if(r == null) return null;

        return Producto.builder()
                .nombre(r.nombre())
                .categoria(r.categoria())
                .precio(r.precio())
                .stock(r.stock())
                .isDeleted(Boolean.FALSE)
                .build();
    }

    public static ProductoResponse toCreateResponse(Producto p) {

        if(p == null) return null;

        return ProductoResponse.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .categoria(p.getCategoria())
                .precio(p.getPrecio())
                .stock(p.getStock())
                .build();
    }

    public static ProductoResponse toUpdateResponse(Producto p) {

        if(p == null) return null;

        return ProductoResponse.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .categoria(p.getCategoria())
                .precio(p.getPrecio())
                .stock(p.getStock())
                .build();
    }
}
