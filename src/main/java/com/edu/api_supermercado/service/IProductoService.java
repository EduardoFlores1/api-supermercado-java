package com.edu.api_supermercado.service;

import com.edu.api_supermercado.dtos.producto.ProductoRequest;
import com.edu.api_supermercado.dtos.producto.ProductoResponse;

import java.util.List;

public interface IProductoService {
    List<ProductoResponse> listarProductos();
    ProductoResponse buscarPorId(Long id);
    ProductoResponse crearProducto(ProductoRequest request);
    ProductoResponse actualizarProducto(Long id, ProductoRequest request);
    void eliminarProducto(Long id);
}
