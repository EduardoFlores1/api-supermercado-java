package com.edu.api_supermercado.controller;

import com.edu.api_supermercado.dtos.sucursal.SucursalResponse;
import com.edu.api_supermercado.service.ISucursalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
