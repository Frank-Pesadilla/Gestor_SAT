package com.gestor.gestor_sat.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO para respuesta con información del archivo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArchivoResponseDTO {

    private Long idArchivos;
    private String nombreArchivo;
    private String ruta;
    private Long idTramite;
    private String nombreTramite;
    private Long idCliente;
    private String nombreCliente;
    private LocalDateTime fechaSubida;
    private LocalDateTime createdAt;
}