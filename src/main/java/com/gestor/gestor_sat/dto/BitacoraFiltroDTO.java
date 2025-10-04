package com.gestor.gestor_sat.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BitacoraFiltroDTO {
    private Long idUsuario;
    private String accion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}
