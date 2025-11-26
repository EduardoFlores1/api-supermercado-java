package com.edu.api_supermercado.service;

import com.edu.api_supermercado.dtos.producto.ProductoRequest;
import com.edu.api_supermercado.dtos.producto.ProductoResponse;
import org.springframework.data.domain.Page;

public interface IProductoService {
    Page<ProductoResponse> listarProductos(Integer page, Integer size);
    ProductoResponse buscarPorId(Long id);
    ProductoResponse crearProducto(ProductoRequest request);
    ProductoResponse actualizarProducto(Long id, ProductoRequest request);
    void eliminarProducto(Long id);
}
