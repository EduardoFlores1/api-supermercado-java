package com.edu.api_supermercado.exception.models.producto;

public class ProductoNoEncontradoException extends RuntimeException{
    private final Long productoId;

    public ProductoNoEncontradoException(Long productoId) {
        super("Producto no encontrado con id: " + productoId);
        this.productoId = productoId;
    }

    public Long getProductoId() {
        return this.productoId;
    }
}
