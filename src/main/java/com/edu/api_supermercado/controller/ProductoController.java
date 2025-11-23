package com.edu.api_supermercado.controller;

import com.edu.api_supermercado.dtos.producto.ProductoRequest;
import com.edu.api_supermercado.dtos.producto.ProductoResponse;
import com.edu.api_supermercado.service.IProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private IProductoService productoService;

    @GetMapping
    public ResponseEntity<Page<ProductoResponse>> listar(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(
                productoService.listarProductos(page, size)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(
                productoService.buscarPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(
            @RequestBody @Valid ProductoRequest request
            ) {

        var productoResponse = productoService.crearProducto(request);
        return ResponseEntity.created(
                URI.create("/api/productos/" + productoResponse.getId()))
                .body(productoResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProductoRequest request
    ) {
        return ResponseEntity.ok(
                productoService.actualizarProducto(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductoResponse> eliminarById(
            @PathVariable Long id
    ) {
        return ResponseEntity.noContent().build();
    }
}
