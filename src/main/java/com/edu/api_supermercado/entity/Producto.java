package com.edu.api_supermercado.entity;

import com.edu.api_supermercado.enums.CategoriaProducto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 50)
    private String nombre;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaProducto categoria;
    @Column(nullable = false)
    private BigDecimal precio;
    @Column(nullable = false)
    private Integer stock;
    @Column(nullable = false)
    private Boolean isDeleted;
}
