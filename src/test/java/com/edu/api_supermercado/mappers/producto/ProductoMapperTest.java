package com.edu.api_supermercado.mappers.producto;
import static org.junit.jupiter.api.Assertions.*;

import com.edu.api_supermercado.dtos.producto.ProductoRequest;
import com.edu.api_supermercado.entity.Producto;
import com.edu.api_supermercado.enums.CategoriaProducto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class ProductoMapperTest {

    // toListResponse

    @Test
    void toListResponse_whenProductIsNull_returnsNull() {
        // When
        var result = ProductoMapper.toListResponse(null);

        // Then
        assertNull(result);
    }

    @Test
    void toListResponse_whenProductFieldsIsValid() {
        // Given
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Coca Cola")
                .categoria(CategoriaProducto.BEBIDAS)
                .precio(new BigDecimal("3.5"))
                .stock(10)
                .build();

        // When
        var response = ProductoMapper.toListResponse(producto);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Coca Cola", response.getNombre());
        assertEquals(CategoriaProducto.BEBIDAS, response.getCategoria());
        assertEquals(new BigDecimal("3.5"), response.getPrecio());
        assertEquals(10, response.getStock());
    }

    // toFindByIdResponse

    @Test
    void toFindByIdResponse_returnsNull_whenInputIsNull() {
        // When
        var response = ProductoMapper.toFindByIdResponse(null);

        // Then
        assertNull(response);
    }

    @Test
    void toFindByIdResponse_whenProductFieldsIsValid() {
        // Given
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Coca Cola")
                .categoria(CategoriaProducto.BEBIDAS)
                .precio(new BigDecimal("3.5"))
                .stock(10)
                .isDeleted(Boolean.FALSE)
                .build();

        // When
        var response = ProductoMapper.toFindByIdResponse(producto);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Coca Cola", response.getNombre());
        assertEquals(CategoriaProducto.BEBIDAS, response.getCategoria());
        assertEquals(new BigDecimal("3.5"), response.getPrecio());
        assertEquals(10, response.getStock());
        assertEquals(Boolean.FALSE, response.getIsDeleted());
    }

    // toCreateEntity

    @Test
    void toCreateEntity_returnsNull_whenProductoRequestIsNull() {
        // When
        var productoEntity = ProductoMapper.toCreateEntity(null);

        // Then
        assertNull(productoEntity);
    }

    @Test
    void toCreateEntity_whenProductRequestIsValid() {
        // Given
        var productoRequest = new ProductoRequest(
                "Coca Cola",
                CategoriaProducto.BEBIDAS,
                new BigDecimal("3.5"),
                10
        );
        // When
        var productoEntity = ProductoMapper.toCreateEntity(productoRequest);

        // Then
        assertNotNull(productoEntity);
        assertEquals("Coca Cola", productoEntity.getNombre());
        assertEquals(CategoriaProducto.BEBIDAS, productoEntity.getCategoria());
        assertEquals(new BigDecimal("3.5"), productoEntity.getPrecio());
        assertEquals(10, productoEntity.getStock());
        assertFalse(productoEntity.getIsDeleted());
    }

    // toCreateResponse

    @Test
    void toCreateResponse_returnsNull_whenProductIsNull() {
        // When
        var pCreateResponse = ProductoMapper.toCreateResponse(null);

        // Then
        assertNull(pCreateResponse);
    }

    @Test
    void toCreateResponse_whenProductFieldsIsValid() {
        // Given
        var pEntity = Producto.builder()
                .id(1L)
                .nombre("Coca Cola")
                .categoria(CategoriaProducto.BEBIDAS)
                .precio(new BigDecimal("3.5"))
                .stock(10)
                .build();

        // When
        var pCreateResponse = ProductoMapper.toCreateResponse(pEntity);

        // Then
        assertNotNull(pCreateResponse);
        assertEquals(1L, pCreateResponse.getId());
        assertEquals("Coca Cola", pCreateResponse.getNombre());
        assertEquals(CategoriaProducto.BEBIDAS, pCreateResponse.getCategoria());
        assertEquals(new BigDecimal("3.5"), pCreateResponse.getPrecio());
        assertEquals(10, pCreateResponse.getStock());
    }

    // toUpdateResponse

    @Test
    void toUpdateResponse_returnsNull_whenProductIsNull() {
        // When
        var pUpdateResponse = ProductoMapper.toUpdateResponse(null);

        // Then
        assertNull(pUpdateResponse);
    }

    @Test
    void toUpdateResponse_whenProductFieldsIsValid() {
        // Given
        var pEntity = Producto.builder()
                .id(1L)
                .nombre("Coca Cola")
                .categoria(CategoriaProducto.BEBIDAS)
                .precio(new BigDecimal("3.5"))
                .stock(10)
                .build();

        // When
        var pUpdateResponse = ProductoMapper.toUpdateResponse(pEntity);

        // Then
        assertNotNull(pUpdateResponse);
        assertEquals(1L, pUpdateResponse.getId());
        assertEquals("Coca Cola", pUpdateResponse.getNombre());
        assertEquals(CategoriaProducto.BEBIDAS, pUpdateResponse.getCategoria());
        assertEquals(new BigDecimal("3.5"), pUpdateResponse.getPrecio());
        assertEquals(10, pUpdateResponse.getStock());
    }
}