package com.edu.api_supermercado.service.impl;

import com.edu.api_supermercado.dtos.sucursal.SucursalRequest;
import com.edu.api_supermercado.dtos.sucursal.SucursalResponse;
import com.edu.api_supermercado.exception.models.sucursal.SucursalNoEncontradaException;
import com.edu.api_supermercado.mappers.sucursal.SucursalMapper;
import com.edu.api_supermercado.repository.ISucursalRepository;
import com.edu.api_supermercado.service.ISucursalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SucursalService implements ISucursalService {

    private final ISucursalRepository sucursalRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<SucursalResponse> listarSucursales(Integer page, Integer size) {

        log.info("[GET]: Iniciando listado de sucursales");

        Pageable pageable = PageRequest.of(page, size);

        var list = sucursalRepository.findAll(pageable)
                .map(SucursalMapper::toResponseFromEntity);

        log.info("[GET]: Listado finalizado exitosamente");

        return list;
    }

    @Override
    @Transactional(readOnly = false)
    public SucursalResponse crearSucursal(SucursalRequest request) {

        log.info("[POST]: Creando sucursal, nombre: {}", request.nombre());

        var newSucursalEntity = sucursalRepository.save(
                SucursalMapper.toCreateFromRequest(request)
        );

        log.info("[POST]: Sucursal creada exitosamente, nombre: {}", request.nombre());

        return SucursalMapper.toResponseFromEntity(newSucursalEntity);
    }

    @Override
    @Transactional(readOnly = false)
    public SucursalResponse actualizarSucursal(Long id, SucursalRequest request) {

        log.info("[PUT]: Actualizando sucursal, id: {}", id);

        var sucursalFind = sucursalRepository.findById(id)
                        .orElseThrow(() -> new SucursalNoEncontradaException(id));

        sucursalFind.setNombre(request.nombre());
        sucursalFind.setDireccion(request.direccion());
        sucursalFind.setTelefono(request.telefono());

        log.info("[PUT]: Sucursal actualizada exitosamente, id: {}", id);

        return SucursalMapper.toResponseFromEntity(sucursalFind);
    }

    @Override
    public void eliminarSucursal(Long id) {

    }
}
