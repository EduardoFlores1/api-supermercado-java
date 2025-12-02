package com.edu.api_supermercado.mappers.sucursal;

import com.edu.api_supermercado.entity.Sucursal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SucursalMapperTest {

    private Sucursal sucursalEntity;

    @BeforeEach
    void setUp() {
        this.sucursalEntity = Sucursal.builder()
                .id(1L)
                .nombre("Ica")
                .direccion("Calle Ica")
                .telefono("987654321")
                .build();
    }

    @Test
    @DisplayName("toResponseFromEntity: retorna nulo cuando argumento es nulo")
    void toResponseFromEntity_null_whenArgIsNull() {

        var result = SucursalMapper.toResponseFromEntity(null);

        assertNull(result);
    }

    @Test
    @DisplayName("toResponseFromEntity: retorna un response cuando la entidad es corecta")
    void toResponseFromEntity_ok_whenArgIsNotNull() {

        var result = SucursalMapper.toResponseFromEntity(sucursalEntity);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Ica", result.getNombre());
        assertEquals("Calle Ica", result.getDireccion());
        assertEquals("987654321", result.getTelefono());
    }
}