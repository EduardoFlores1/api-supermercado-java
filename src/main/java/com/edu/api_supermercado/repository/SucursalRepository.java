package com.edu.api_supermercado.repository;

import com.edu.api_supermercado.entity.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
}
