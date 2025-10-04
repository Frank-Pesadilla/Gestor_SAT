package com.gestor.gestor_sat.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BitacoraDTO {
    private Long id;
    private String nombreUsuario;
    private String accion;
    private String detalles;
    private String ipAddress;
    private LocalDateTime fecha;
}

