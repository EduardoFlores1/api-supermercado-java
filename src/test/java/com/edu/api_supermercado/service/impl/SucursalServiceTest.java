package com.edu.api_supermercado.service.impl;

import com.edu.api_supermercado.dtos.sucursal.SucursalRequest;
import com.edu.api_supermercado.dtos.sucursal.SucursalResponse;
import com.edu.api_supermercado.entity.Sucursal;
import com.edu.api_supermercado.repository.ISucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SucursalServiceTest {

    @Mock
    private ISucursalRepository sucursalRepository;

    @InjectMocks
    private SucursalService sucursalService;

    private Sucursal sucursalEntity;
    private Sucursal sucursalEntity2;
    private SucursalRequest sucursalRequest;

    @BeforeEach
    void setUp() {
        this.sucursalEntity = Sucursal.builder()
                .id(1L)
                .nombre("Ica")
                .direccion("Calle Ica")
                .telefono("987654321")
                .build();

        this.sucursalEntity2 = Sucursal.builder()
                .id(1L)
                .nombre("Pisco")
                .direccion("Calle Pisco")
                .telefono("987654321")
                .build();

        this.sucursalRequest = new SucursalRequest(
                "Ica",
                "Calle Ica",
                "987654321"
        );
    }

    @Test
    void listarSucursales_ok() {

        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);

        var sucursales = List.of(sucursalEntity, sucursalEntity2);

        Page<Sucursal> sucursalPage = new PageImpl<>(sucursales, pageable, sucursales.size());

        when(sucursalRepository.findAll(pageable)).thenReturn(sucursalPage);

        // when
        Page<SucursalResponse> result = sucursalService.listarSucursales(page, size);

        // then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getContent().size());

        verify(sucursalRepository, times(1)).findAll(pageable);
    }

    @Test
    void listarSucursales_ok_emptyPage() {
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);

        Page<Sucursal> emptyPage = Page.empty(pageable);

        when(sucursalRepository.findAll(pageable)).thenReturn(emptyPage);

        // when
        Page<SucursalResponse> result = sucursalService.listarSucursales(page, size);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());

        verify(sucursalRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("crearSucursal: Retorna 200 ok, cuando request es valid")
    void crearSucursal_ok() {

        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(sucursalEntity);

        // when & then
        var response = sucursalService.crearSucursal(sucursalRequest);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(sucursalRequest.nombre(), response.getNombre());
        assertEquals(sucursalRequest.direccion(), response.getDireccion());
        assertEquals(sucursalRequest.telefono(), response.getTelefono());

        verify(sucursalRepository, times(1)).save(any(Sucursal.class));

    }

    @Test
    void actualizarSucursal() {
    }

    @Test
    void eliminarSucursal() {
    }
}