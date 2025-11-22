package com.edu.api_supermercado.repository;

import com.edu.api_supermercado.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}
