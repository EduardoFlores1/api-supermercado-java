package com.edu.api_supermercado.controller;

import com.edu.api_supermercado.dtos.sucursal.SucursalRequest;
import com.edu.api_supermercado.dtos.sucursal.SucursalResponse;
import com.edu.api_supermercado.entity.Sucursal;
import com.edu.api_supermercado.service.ISucursalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
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

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ISucursalService sucursalService;

    private Sucursal sucursalEntity;
    private SucursalResponse sucursalResponse;
    private SucursalRequest sucursalRequest;

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

        this.sucursalRequest = new SucursalRequest(
                "Ica",
                "Calle Ica",
                "987654321"
        );
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

    @Test
    @DisplayName("crear: Crea una sucursal con argumentos validos")
    void crear_ok() throws Exception{

        when(sucursalService.crearSucursal(any(SucursalRequest.class))).thenReturn(sucursalResponse);

        // when + then

        mockMvc.perform(post("/api/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sucursalRequest)))

                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/sucursales/" + sucursalResponse.getId()))
                .andExpect(jsonPath("$.id").value(sucursalResponse.getId()))
                .andExpect(jsonPath("$.nombre").value("Pisco"))
                .andExpect(jsonPath("$.direccion").value("Calle Pisco"))
                .andExpect(jsonPath("$.telefono").value("987654321"));

        verify(sucursalService, times(1)).crearSucursal(any(SucursalRequest.class));
    }

    @Test
    @DisplayName("crear: Crea una sucursal con argumentos inválidos")
    void crear_errorValid() throws Exception {

        String msgErrorDetail = "Error de validación en uno o más campos";
        SucursalRequest requestInvalido = new SucursalRequest(
                "",
                "",
                "98765432"
        );

        mockMvc.perform(post("/api/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido)))

                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isBadRequest())

                .andExpect(jsonPath("$.title").value("Error De Validación"))
                .andExpect(jsonPath("$.detail").value(msgErrorDetail))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.nombre").exists())
                .andExpect(jsonPath("$.errors.direccion").exists())
                .andExpect(jsonPath("$.errors.telefono").exists());

        verify(sucursalService, never()).crearSucursal(any(SucursalRequest.class));
    }


}