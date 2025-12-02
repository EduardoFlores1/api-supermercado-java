package com.edu.api_supermercado.service.impl;

import com.edu.api_supermercado.dtos.sucursal.SucursalRequest;
import com.edu.api_supermercado.dtos.sucursal.SucursalResponse;
import com.edu.api_supermercado.mappers.sucursal.SucursalMapper;
import com.edu.api_supermercado.repository.ISucursalRepository;
import com.edu.api_supermercado.service.ISucursalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SucursalService implements ISucursalService {

    private final ISucursalRepository sucursalRepository;

    @Override
    public Page<SucursalResponse> listarSucursales(Integer page, Integer size) {

        log.info("[GET]: Iniciando listado de sucursales");

        Pageable pageable = PageRequest.of(page, size);

        var list = sucursalRepository.findAll(pageable)
                .map(SucursalMapper::toResponseFromEntity);

        log.info("[GET]: Listado finalizado exitosamente");

        return list;
    }

    @Override
    public SucursalResponse crearSucursal(SucursalRequest request) {
        return null;
    }

    @Override
    public SucursalResponse actualizarSucursal(Long id, SucursalRequest request) {
        return null;
    }

    @Override
    public void eliminarSucursal(Long id) {

    }
}
