package com.gestor.gestor_sat.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO para información de archivos en el historial de trámites
 * CU-SAT005: Historial de Trámites - Tarea 4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArchivoHistorialDTO {

    private Long idArchivo;
    private String nombreArchivo;
    private String ruta;
    private LocalDateTime fechaSubida;
}
