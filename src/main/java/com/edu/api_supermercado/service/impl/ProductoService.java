package com.edu.api_supermercado.service.impl;

import com.edu.api_supermercado.dtos.producto.ProductoRequest;
import com.edu.api_supermercado.dtos.producto.ProductoResponse;
import com.edu.api_supermercado.exception.models.producto.ProductoEliminadoException;
import com.edu.api_supermercado.exception.models.producto.ProductoNoEncontradoException;
import com.edu.api_supermercado.mappers.producto.ProductoMapper;
import com.edu.api_supermercado.repository.IProductoRepository;
import com.edu.api_supermercado.service.IProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductoService implements IProductoService {

    private final IProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponse> listarProductos(Integer page, Integer size) {

        log.info("[GET]: Iniciando listado de productos");

        Pageable pageable = PageRequest.of(page, size);

        var result = productoRepository.findAll(pageable)
                .map(ProductoMapper::toListResponse);


        log.info("[GET]: Listado finalizado exitosamente");

        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public ProductoResponse buscarPorId(Long id) {

        log.info("[GET]: Buscando Producto por id: {}", id);

        var pResponse = productoRepository.findById(id)
                .map(ProductoMapper::toFindByIdResponse)
                        .orElseThrow(() -> new ProductoNoEncontradoException(id));

        log.info("[GET]: Producto encontrado exitosamente, id: {}", id);

        return pResponse;
    }

    @Transactional(readOnly = false)
    @Override
    public ProductoResponse crearProducto(ProductoRequest request) {

        log.info("[POST]: Creando producto, nombre: {}", request.nombre());

        var newProductEntity = productoRepository.save(
                ProductoMapper.toCreateEntity(request));

        log.info("[POST]: Producto creado exitosamente, nombre: {}", request.nombre());

        return ProductoMapper.toCreateResponse(newProductEntity);
    }

    @Transactional(readOnly = false)
    @Override
    public ProductoResponse actualizarProducto(Long id, ProductoRequest request) {

        log.info("[PUT]: Actualizando producto, id: {}", id);

        var productFind = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));

        if (productFind.getIsDeleted())
            throw new ProductoEliminadoException(id);

        productFind.setNombre(request.nombre());
        productFind.setCategoria(request.categoria());
        productFind.setPrecio(request.precio());
        productFind.setStock(request.stock());

        log.info("[PUT]: Producto actualizado exitosamente, id: {}", id);

        return ProductoMapper.toUpdateResponse(productFind);
    }

    @Override
    @Transactional(readOnly = false)
    public void eliminarProducto(Long id) {

        log.info("[DELETE]: Eliminando producto con id: {}", id);

        var productFind = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));

        if (productFind.getIsDeleted())
            throw new ProductoEliminadoException(id);

        productFind.setIsDeleted(Boolean.TRUE);

        log.info("[DELETE]: Producto eliminado exitosamente, id: {}", id);
    }
}