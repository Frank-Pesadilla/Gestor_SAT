package com.gestor.gestor_sat.dto;

import com.gestor.gestor_sat.entity.enums.TramiteEstado;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para información de consultas de trámite en el historial
 * CU-SAT005: Historial de Trámites - Tarea 1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaTramiteHistorialDTO {

    private Long idConsultaTramite;
    private LocalDate fechaTramite;
    private TramiteEstado estado;
    private String estadoDescripcion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
