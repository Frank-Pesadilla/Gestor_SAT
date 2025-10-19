package com.gestor.gestor_sat.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO para respuesta de tipo de trámite
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoTramiteResponseDTO {

    private Long idTipoTramite;
    private String portal;
    private String link;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}