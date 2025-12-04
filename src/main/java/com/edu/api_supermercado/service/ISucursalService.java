package com.edu.api_supermercado.service;

import com.edu.api_supermercado.dtos.sucursal.SucursalRequest;
import com.edu.api_supermercado.dtos.sucursal.SucursalResponse;
import org.springframework.data.domain.Page;

public interface ISucursalService {
    Page<SucursalResponse> listarSucursales(Integer page, Integer size);
    SucursalResponse crearSucursal(SucursalRequest request);
    SucursalResponse actualizarSucursal(Long id, SucursalRequest request);
    void eliminarSucursal(Long id);
}
