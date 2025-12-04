package com.edu.api_supermercado.controller;

import com.edu.api_supermercado.dtos.sucursal.SucursalRequest;
import com.edu.api_supermercado.dtos.sucursal.SucursalResponse;
import com.edu.api_supermercado.service.ISucursalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
@Slf4j
public class SucursalController {

    private final ISucursalService sucursalService;

    @GetMapping
    public ResponseEntity<Page<SucursalResponse>> listar(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {

        log.info("[GET-SucursalController]: llamando al controlador listar");

        return ResponseEntity.ok(sucursalService.listarSucursales(page, size));
    }

    @PostMapping
    public ResponseEntity<SucursalResponse> crear(
            @RequestBody @Valid SucursalRequest request
            ) {

        log.info("[POST-SucursalController]: llamando al controlador crear");

        var response = sucursalService.crearSucursal(request);

        return ResponseEntity.created(URI.create("/api/sucursales/" + response.getId()))
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SucursalResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid SucursalRequest request
    ) {

        log.info("[PUT-SucursalController]: llamando al controlador update");

        return ResponseEntity.ok(sucursalService.actualizarSucursal(id, request));
    }
}
