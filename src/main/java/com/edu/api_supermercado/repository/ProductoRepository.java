package com.edu.api_supermercado.repository;

import com.edu.api_supermercado.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
