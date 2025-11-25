package com.edu.api_supermercado.service.impl;

import com.edu.api_supermercado.db.DBTest;
import com.edu.api_supermercado.dtos.producto.ProductoRequest;
import com.edu.api_supermercado.dtos.producto.ProductoResponse;
import com.edu.api_supermercado.entity.Producto;
import com.edu.api_supermercado.enums.CategoriaProducto;
import com.edu.api_supermercado.exception.models.producto.ProductoEliminadoException;
import com.edu.api_supermercado.exception.models.producto.ProductoNoEncontradoException;
import com.edu.api_supermercado.repository.IProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private IProductoRepository productoRepository;

    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        productoService = new ProductoService(productoRepository);
    }

    @Test
    void listarProductos() {
        // Given
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Producto p = DBTest.entity();

        Page<Producto> pageResult = new PageImpl<>(List.of(p), pageable, 1);

        Mockito.when(productoRepository.findAll(pageable)).thenReturn(pageResult);

        // When
        Page<ProductoResponse> response = productoService.listarProductos(page, size);

        // Then
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getContent().size());

        ProductoResponse pr = response.getContent().get(0);

        assertEquals(p.getId(), pr.getId());
        assertEquals(p.getNombre(), pr.getNombre());
        assertEquals(p.getCategoria(), pr.getCategoria());
        assertEquals(p.getPrecio(), pr.getPrecio());
        assertEquals(p.getStock(), pr.getStock());

        Mockito.verify(productoRepository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    void buscarPorId_returnsProductoNoEncontradoException_whenNotExists() {
        // Given
        Long id = 99L;

        Mockito.when(productoRepository.findById(id)).thenReturn(Optional.empty());

        // When + Then
        assertThrows(
                ProductoNoEncontradoException.class,
                () -> productoService.buscarPorId(id)
        );

        Mockito.verify(productoRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void buscarPorId_returnsProductoResponse_whenExists() {
        // Given
        Long id = 1L;

        Producto p = DBTest.entity();

        Mockito.when(productoRepository.findById(id)).thenReturn(Optional.of(p));

        // When
        ProductoResponse response = productoService.buscarPorId(id);

        // Then
        assertNotNull(response);
        assertEquals(p.getId(), response.getId());
        assertEquals(p.getNombre(), response.getNombre());
        assertEquals(p.getPrecio(), response.getPrecio());
        assertEquals(p.getCategoria(), response.getCategoria());
        assertEquals(p.getStock(), response.getStock());
        assertEquals(p.getIsDeleted(), response.getIsDeleted());

        Mockito.verify(productoRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void crearProducto() {
        // Given
        ProductoRequest request = new ProductoRequest(
                "Coca Cola",
                CategoriaProducto.BEBIDAS,
                BigDecimal.valueOf(3.5),
                10
        );

        // Lo que debería retornarse desde save()
        Producto pGuardado = Producto.builder()
                .id(1L)
                .nombre("Coca Cola")
                .categoria(CategoriaProducto.BEBIDAS)
                .precio(BigDecimal.valueOf(3.5))
                .stock(10)
                .isDeleted(false)
                .build();

        Mockito.when(productoRepository.save(Mockito.any(Producto.class)))
                .thenReturn(pGuardado);

        // When
        ProductoResponse response = productoService.crearProducto(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Coca Cola", response.getNombre());
        assertEquals(BigDecimal.valueOf(3.5), response.getPrecio());
        assertEquals(CategoriaProducto.BEBIDAS, response.getCategoria());
        assertEquals(10, response.getStock());

        // Verifica que el save() fue llamado solo una vez con una entidad construida desde el request
        Mockito.verify(productoRepository, Mockito.times(1))
                .save(Mockito.argThat(p ->
                        p.getNombre().equals(request.nombre()) &&
                        p.getCategoria().equals(request.categoria()) &&
                        p.getPrecio().equals(request.precio()) &&
                        p.getStock().equals(request.stock()) &&
                        !p.getIsDeleted()

                        ));
    }

    @Test
    void actualizarProducto_returnsProductoNoEncontradoException_whenNotExists() {
        // Given
        Long id = 99L;

        // When + then
        Mockito.when(productoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                ProductoNoEncontradoException.class,
                () -> productoService.actualizarProducto(id, Mockito.any(ProductoRequest.class))
        );

        Mockito.verify(productoRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void actualizarProducto_returnsProductoEliminadoException_whenIsDeleted() {
        // Given
        Long id = 99L;
        Producto pEliminado = Producto.builder()
                .id(id)
                .isDeleted(Boolean.TRUE)
                .build();

        // When + Then
        Mockito.when(productoRepository.findById(id)).thenReturn(Optional.of(pEliminado));

        assertThrows(
                ProductoEliminadoException.class,
                () -> productoService.actualizarProducto(id, Mockito.any(ProductoRequest.class))
        );

        Mockito.verify(productoRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void actualizarProducto() {
        // Given
        Long id = 1L;

        ProductoRequest request = new ProductoRequest(
                "Coca Cola",
                CategoriaProducto.BEBIDAS,
                BigDecimal.valueOf(3.5),
                10
        );

        Producto existingProduct = Producto.builder()
                .id(id)
                .nombre("Viejo Nombre")
                .categoria(CategoriaProducto.BEBIDAS)
                .precio(BigDecimal.valueOf(3.5))
                .stock(11)
                .isDeleted(false)
                .build();

        Mockito.when(productoRepository.findById(id)).thenReturn(Optional.of(existingProduct));

        // When
        ProductoResponse response = productoService.actualizarProducto(id, request);

        // Then
        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("Coca Cola", response.getNombre());
        assertEquals(CategoriaProducto.BEBIDAS, response.getCategoria());
        assertEquals(new BigDecimal("3.5"), response.getPrecio());
        assertEquals(10, response.getStock());

        // Verifica que findById se llamó exactamente una vez
        Mockito.verify(productoRepository, Mockito.times(1)).findById(id);

    }

    @Test
    void eliminarProducto_returnsProductoNoEncontradoExepcion_whenNotExists() {
        // Given
        Long id = 99L;

        Mockito.when(productoRepository.findById(id)).thenReturn(Optional.empty());

        // When + Then
        assertThrows(
                ProductoNoEncontradoException.class,
                () -> productoService.eliminarProducto(id)
        );

        Mockito.verify(productoRepository).findById(id);
        Mockito.verifyNoMoreInteractions(productoRepository);
    }

    @Test
    void eliminarProducto_returnsProductoEliminadoExepcion_whenIsDeleted() {
        // Given
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Coca Cola")
                .isDeleted(true)
                .build();

        Mockito.when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // When + Then
        assertThrows(ProductoEliminadoException.class,
                () -> productoService.eliminarProducto(1L));

        Mockito.verify(productoRepository).findById(1L);

        // El test confirma que NO se actualiza nada más
        Mockito.verifyNoMoreInteractions(productoRepository);
    }

    @Test
    void eliminarProducto() {
        // Given
        Long id = 1L;
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Coca Cola")
                .isDeleted(false)
                .build();

        Mockito.when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // When
        productoService.eliminarProducto(id);

        // Then
        assertTrue(producto.getIsDeleted(), "Debe modificarse a true");

        Mockito.verify(productoRepository).findById(id);
        Mockito.verifyNoMoreInteractions(productoRepository);
    }
}