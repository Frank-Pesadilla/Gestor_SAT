package com.gestor.gestor_sat.dto;

import com.gestor.gestor_sat.entity.enums.TramiteEstado;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para el historial completo de trámites de un cliente
 * CU-SAT005: Historial de Trámites - Tarea 1
 *
 * Contiene toda la información del trámite incluyendo:
 * - Datos del trámite
 * - Información de la consulta
 * - Tipo de trámite
 * - Archivos adjuntos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialTramiteDTO {

    // Información de la consulta del trámite
    private Long idConsultaTramite;
    private LocalDate fechaTramite;
    private TramiteEstado estadoActual;
    private String estadoDescripcion;

    // Información del trámite
    private Long idTramite;
    private String nombreTramite;
    private String descripcionTramite;

    // Información del tipo de trámite
    private Long idTipoTramite;
    private String portal; // SAT, RENAP, IGSS, MINTRAB, OTRO
    private String linkPortal;

    // Información del cliente (para contexto)
    private Long idCliente;
    private String nombreCliente;
    private String dpiCliente;

    // Tarea 4: Lista de archivos asociados al trámite
    private List<ArchivoHistorialDTO> archivosAdjuntos;

    // Fechas de auditoría
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
