package com.edu.api_supermercado.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Sucursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 50)
    private String nombre;
    @Column(nullable = false, length = 150)
    private String direccion;
    @Column(nullable = false, length = 9)
    private String telefono;
}
