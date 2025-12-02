package com.edu.api_supermercado.controller;

import com.edu.api_supermercado.dtos.sucursal.SucursalResponse;
import com.edu.api_supermercado.entity.Sucursal;
import com.edu.api_supermercado.service.ISucursalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SucursalController.class)
class SucursalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ISucursalService sucursalService;

    private Sucursal sucursalEntity;
    private SucursalResponse sucursalResponse;

    @BeforeEach
    void setUp() {
        this.sucursalEntity = Sucursal.builder()
                .id(1L)
                .nombre("Ica")
                .direccion("Calle Ica")
                .telefono("987654321")
                .build();

        this.sucursalResponse = SucursalResponse.builder()
                .id(2L)
                .nombre("Pisco")
                .direccion("Calle Pisco")
                .telefono("987654321")
                .build();
    }

    @Test
    @DisplayName("listar - lista con los parámetros por defecto")
    void listar_ok_defaultParameters() throws Exception{

        Page<SucursalResponse> pageResult = new PageImpl<>(
                List.of(sucursalResponse),
                PageRequest.of(0,10),
                1
        );

        when(sucursalService.listarSucursales(0,10)).thenReturn(pageResult);

        // when & then
        mockMvc.perform(get("/api/sucursales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(2))
                .andExpect(jsonPath("$.content[0].nombre").value("Pisco"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));

        verify(sucursalService, times(1)).listarSucursales(0, 10);
    }

    @Test
    @DisplayName("listar - lista con una page and size personalizada")
    void listar_ok_customPageAndSize() throws Exception {

        Page<SucursalResponse> pageResult = new PageImpl<>(
                List.of(sucursalResponse),
                PageRequest.of(2,5),
                11
        );

        when(sucursalService.listarSucursales(2,5)).thenReturn(pageResult);

        // when & then
        mockMvc.perform(get("/api/sucursales")
                .param("page", "2")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(2))
                .andExpect(jsonPath("$.totalElements").value(11))
                .andExpect(jsonPath("$.totalPages").value(3));

        verify(sucursalService, times(1)).listarSucursales(2,5);
    }
}