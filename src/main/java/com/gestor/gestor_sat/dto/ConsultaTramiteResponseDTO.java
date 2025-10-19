package com.gestor.gestor_sat.dto;

import com.gestor.gestor_sat.entity.enums.TramiteEstado;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para respuesta de consulta de trámite
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaTramiteResponseDTO {

    private Long idConsultaTramite;
    private Long idTramites;
    private String nombreTramite;
    private Long idCliente;
    private String nombreCliente;
    private LocalDate fechaTramite;
    private TramiteEstado estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}