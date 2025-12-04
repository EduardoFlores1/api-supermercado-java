package com.edu.api_supermercado.exception.models.sucursal;

public class SucursalNoEncontradaException extends RuntimeException{
    private final Long sucursalId;

    public SucursalNoEncontradaException(Long sucursalId) {
        super("Sucursal no encontrada con id: " + sucursalId);
        this.sucursalId = sucursalId;
    }

    public Long getSucursalId() {
        return this.sucursalId;
    }
}
