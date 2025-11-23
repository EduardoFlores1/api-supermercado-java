package com.edu.api_supermercado.entity;

import com.edu.api_supermercado.enums.EstadoVenta;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate fecha;
    @Column(nullable = false)
    private EstadoVenta estado;
    @Column(nullable = false)
    private BigDecimal total;
    @Column(nullable = false)
    private Boolean isDeleted;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sucursalId", nullable = false)
    private Sucursal sucursal;

    // Eliminación logica de venta, no aplicable orphan
    // Las operaciones de cascada limitadas a persistencia para auditoria
    @OneToMany(mappedBy = "venta", cascade = {CascadeType.PERSIST},
                orphanRemoval = false, fetch = FetchType.EAGER)
    private List<DetalleVenta> detalle = new ArrayList<>();

    public BigDecimal calcularTotal() {
        return detalle.stream()
                .map(DetalleVenta::calcularSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
