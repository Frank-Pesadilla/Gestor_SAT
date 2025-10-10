package com.gestor.gestor_sat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity @Table(name = "bitacora")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Bitacora {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bitacora")
    private Long idBitacora;

    @Column(name = "usuario", length = 150)
    private String usuario;

    @Column(name = "accion", nullable = false, length = 100)
    private String accion;

    @Column(name = "detalles", columnDefinition = "TEXT")
    private String detalles;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @CreationTimestamp
    @Column(name = "fecha", nullable = false, updatable = false)
    private LocalDateTime fecha;
}
