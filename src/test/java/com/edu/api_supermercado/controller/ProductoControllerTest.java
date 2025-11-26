package com.edu.api_supermercado.controller;

import com.edu.api_supermercado.dtos.producto.ProductoRequest;
import com.edu.api_supermercado.dtos.producto.ProductoResponse;
import com.edu.api_supermercado.enums.CategoriaProducto;
import com.edu.api_supermercado.exception.models.producto.ProductoEliminadoException;
import com.edu.api_supermercado.exception.models.producto.ProductoNoEncontradoException;
import com.edu.api_supermercado.service.IProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IProductoService productoService;

    private ProductoRequest productoRequestValido;
    private ProductoResponse productoResponse;

    @BeforeEach
    void setUp() {
        // Objeto request válido
        productoRequestValido = new ProductoRequest(
                "Coca Cola",
                CategoriaProducto.BEBIDAS,
                new BigDecimal("3.5"),
                10
        );

        // Objeto de prueba común
        this.productoResponse = ProductoResponse.builder()
                .id(1L)
                .nombre("Coca Cola")
                .categoria(CategoriaProducto.BEBIDAS)
                .precio(new BigDecimal("3.5"))
                .stock(10)
                .isDeleted(false)
                .build();
    }

    @Test
    @DisplayName("Listar Productos - Debe retornar estado http 200 y una página de productos")
    void listar_cuandoExistenDatos() throws Exception {
        // Given: Datos simulados
        Pageable pageable = PageRequest.of(0, 10);
        List<ProductoResponse> lista = List.of(productoResponse);

        // Creamos una page simulada (Spring data)
        Page<ProductoResponse> pageResult = new PageImpl<>(lista, pageable, 1);

        // Simulamos que el servicio devuelve esta página
        Mockito.when(productoService.listarProductos(Mockito.anyInt(), Mockito.anyInt())).thenReturn(pageResult);

        // When & Then: Ejecutamos la petición y verificamos
        mockMvc.perform(get("/api/productos")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Esperamos 200 ok
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verificamos estructura JSON de respuesta (Spring page devuelve un objeto "content")
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].nombre").value("Coca Cola"))
                .andExpect(jsonPath("$.content[0].categoria").value("BEBIDAS"))
                .andExpect(jsonPath("$.content[0].precio").value(3.5))
                .andExpect(jsonPath("$.content[0].stock").value(10))
                .andExpect(jsonPath("$.totalElements").value(1));

        // Verificamos que el servicio fue llamado
        Mockito.verify(productoService).listarProductos(0, 10);
    }

    @Test
    @DisplayName("Listar Productos - Debe usar paginacion por defecto si no se envian parametros")
    void listar_paginacionPorDefecto() throws Exception {
        // Given
        Page<ProductoResponse> pageEmpty = new PageImpl<>(List.of());
        Mockito.when(productoService.listarProductos(0,10)).thenReturn(pageEmpty);

        // When
        mockMvc.perform(get("/api/productos")) // Sin params
                .andExpect(status().isOk());

        // Then
        Mockito.verify(productoService).listarProductos(0,10);
    }

    @Test
    @DisplayName("Buscar por ID - Retornará 404 cuando el id no existe")
    void buscarPorId_notFound() throws Exception {
        // Given
        Long idInexistente = 99L;
        String msgError = "Producto no encontrado con id: 99";

        ProductoNoEncontradoException mockEx = new ProductoNoEncontradoException(idInexistente);

        Mockito.when(productoService.buscarPorId(idInexistente))
                .thenThrow(mockEx);

        // When y Then: Ejecutamos y validamos
        mockMvc.perform(get("/api/productos/{id}", idInexistente)
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound()) // 1. Validamos estado 404
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))

                // 2. Validamos el titulo y el detalle del ProblemeDetail
                .andExpect(jsonPath("$.title").value("Producto No Encontrado"))
                .andExpect(jsonPath("$.detail").value(msgError))
                .andExpect(jsonPath("$.status").value(404))

                // 3. Validamos la propiedad personalizada
                .andExpect(jsonPath("$.productoId").value(idInexistente));

        Mockito.verify(productoService).buscarPorId(idInexistente);
    }

    @Test
    @DisplayName("Buscar por ID - Retornará 200 ok y el producto cuando exista")
    void buscarPorId() throws Exception {
        // Given
        Long idValido = 1L;
        Mockito.when(productoService.buscarPorId(idValido))
                .thenReturn(productoResponse);

        // When + Then
        mockMvc.perform(get("/api/productos/{id}", idValido)
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Coca Cola"))
                .andExpect(jsonPath("$.categoria").value("BEBIDAS"))
                .andExpect(jsonPath("$.precio").value(3.5))
                .andExpect(jsonPath("$.stock").value(10));

        Mockito.verify(productoService).buscarPorId(idValido);
    }

    @Test
    @DisplayName("Crear Producto - Debe retornar error 400 Bad Request por error de validacion")
    void crear_errorDeValidacion() throws Exception {
        // Given
        String msgErrorDetail = "Error de validación en uno o más campo";
        ProductoRequest requestInvalido = new ProductoRequest(
                "",
                CategoriaProducto.BEBIDAS,
                new BigDecimal("3.5"),
                0
        );

        // El @valid se encarga de esto antes de que llegue al servicio, asi que no es necesario mockear el servicio

        // When + Then: Petición con cuerpo inválida
        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido)))

        // validaciones
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isBadRequest())
        // validamos el formato de problemDetail
                .andExpect(jsonPath("$.title").value("Error De Validación"))
                .andExpect(jsonPath("$.detail").value(msgErrorDetail))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.nombre").exists())
                .andExpect(jsonPath("$.errors.stock").exists());

        // Verificamos que el servicio no fue llamado
        Mockito.verify(productoService, Mockito.never()).crearProducto(Mockito.any(ProductoRequest.class));
    }

    @Test
    @DisplayName("Crear Producto - Debe retornar 201 cuando el request es válido")
    void crear_requestValida() throws Exception{
        // Given: Simulamos que el servicio retorna la respuesta con id
        Mockito.when(productoService.crearProducto(Mockito.any(ProductoRequest.class)))
                .thenReturn(productoResponse);

        // When + Then: Simula la peticion post
        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoRequestValido)))

                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "/api/productos/" + productoResponse.getId()))
                .andExpect(jsonPath("$.id").value(productoResponse.getId()))
                .andExpect(jsonPath("$.nombre").value("Coca Cola"));

        Mockito.verify(productoService).crearProducto(Mockito.any(ProductoRequest.class));

    }

    @Test
    @DisplayName("Actualizar Producto - Debe retornar 200 y el producto actualizado")
    void actualizar_retorna200_requestValida() throws Exception{
        // Given
        Long idExistente = 1L;
        Mockito.when(productoService.actualizarProducto(Mockito.eq(idExistente), Mockito.any(ProductoRequest.class)))
                .thenReturn(productoResponse);

        // When + Then: Simulamos la peticion PUT
        mockMvc.perform(put("/api/productos/{id}", idExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoRequestValido)))

                // Validaciones
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(idExistente))
                .andExpect(jsonPath("$.nombre").value("Coca Cola"))
                .andExpect(jsonPath("$.precio").value(3.5));

        Mockito.verify(productoService).actualizarProducto(Mockito.eq(idExistente), Mockito.any(ProductoRequest.class));
    }

    @Test
    @DisplayName("Actualizar Producto - Debe retornar 404 cuando el id no existe")
    void actualizar_404_cuandoIdNoExiste() throws Exception{
        // Given
        Long idInexistente = 99L;

        // Simulamos que el servicio lanza una excepcion
        Mockito.when(productoService.actualizarProducto(Mockito.eq(idInexistente), Mockito.any(ProductoRequest.class)))
                .thenThrow(new ProductoNoEncontradoException(idInexistente));

        // When + Then:
        mockMvc.perform(put("/api/productos/{id}", idInexistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoRequestValido)))

                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Producto No Encontrado"))
                .andExpect(jsonPath("$.productoId").value(idInexistente));

        Mockito.verify(productoService).actualizarProducto(Mockito.eq(idInexistente), Mockito.any(ProductoRequest.class));
    }

    @Test
    @DisplayName("Actualizar Producto - Debe retornar 404 cuando esta marcado como eliminado")
    void actualizar_404_cuandoProductoTieneEstadoEliminado() throws Exception{
        // Given
        Long idEliminado = 5L;

        Mockito.when(productoService.actualizarProducto(Mockito.eq(idEliminado), Mockito.any(ProductoRequest.class)))
                .thenThrow(new ProductoEliminadoException(idEliminado));

        // When y Then
        mockMvc.perform(put("/api/productos/{id}", idEliminado)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoRequestValido)))

                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Producto No Disponible"))
                .andExpect(jsonPath("$.productoId").value(idEliminado));

        Mockito.verify(productoService).actualizarProducto(Mockito.eq(idEliminado), Mockito.any(ProductoRequest.class));
    }

    @Test
    @DisplayName("Eliminar Producto - Debe retornar 204 OK al elinar exitosamente")
    void eliminarById_204_cuandoSeEliminaExitosamente() throws Exception{

        // Given
        Long idExistente = 1L;

        Mockito.doNothing().when(productoService).eliminarProducto(idExistente);

        // When y Then
        mockMvc.perform(delete("/api/productos/{id}", idExistente)
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        Mockito.verify(productoService).eliminarProducto(idExistente);
    }

    @Test
    @DisplayName("Eliminar Producto - Debe retornar 404 cuando el id no existe")
    void eliminarById_404_cuandoIdNoExiste() throws Exception{
        // Given
        Long idNoExistente = 1L;

        Mockito.doThrow(new ProductoNoEncontradoException(idNoExistente))
                .when(productoService).eliminarProducto(idNoExistente);

        // When + Then
        mockMvc.perform(delete("/api/productos/{id}", idNoExistente)
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))

                .andExpect(jsonPath("$.title").value("Producto No Encontrado"))
                .andExpect(jsonPath("$.productoId").value(idNoExistente));

        Mockito.verify(productoService).eliminarProducto(idNoExistente);
    }

    @Test
    @DisplayName("Eliminar Producto - Debe retornar 404 cuando el id no existe")
    void eliminarById_404_cuandoProductoEsMarcadoComoEliminado() throws Exception{
        // Given
        Long idEliminado = 5L;

        Mockito.doThrow(new ProductoEliminadoException(idEliminado))
                .when(productoService).eliminarProducto(idEliminado);

        // When + Then
        mockMvc.perform(delete("/api/productos/{id}", idEliminado)
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))

                .andExpect(jsonPath("$.title").value("Producto No Disponible"))
                .andExpect(jsonPath("$.productoId").value(idEliminado));

        Mockito.verify(productoService).eliminarProducto(idEliminado);
    }
}