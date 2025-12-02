package com.edu.api_supermercado.mappers.sucursal;

import com.edu.api_supermercado.dtos.sucursal.SucursalResponse;
import com.edu.api_supermercado.entity.Sucursal;

public class SucursalMapper {

    public static SucursalResponse toResponseFromEntity(Sucursal entity){
        if (entity == null) return null;
        return SucursalResponse.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .direccion(entity.getDireccion())
                .telefono(entity.getTelefono())
                .build();
    };
}
