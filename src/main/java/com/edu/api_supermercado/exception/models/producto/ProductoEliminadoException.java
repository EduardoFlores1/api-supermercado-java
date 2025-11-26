package com.edu.api_supermercado.exception.models.producto;

public class ProductoEliminadoException extends RuntimeException{
    private final Long productoId;

    public ProductoEliminadoException(Long productoId) {
        super("Producto no disponible, id: " + productoId);
        this.productoId = productoId;
    }

    public Long getProductoId() {
        return this.productoId;
    }
}
