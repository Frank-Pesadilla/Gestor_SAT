package com.gestor.gestor_sat.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO para respuesta con información del trámite
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TramiteResponseDTO {

    private Long idTramites;
    private String nombre;
    private String descripcion;
    private Long idTipoTramite;
    private String portalTipoTramite;
    private String linkTipoTramite;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}