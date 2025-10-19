package com.gestor.gestor_sat.dto;

import com.gestor.gestor_sat.entity.enums.TramiteEstado;
import lombok.*;

/**
 * DTO para datos de gráfica de trámites por estado
 * CU-SAT012: Panel de Control - Tarea 9
 *
 * Estructura lista para gráficas de pastel/dona en el frontend
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TramitePorEstadoDTO {

    /**
     * Estado del trámite (INICIADO, PENDIENTE, TERMINADO)
     */
    private TramiteEstado estado;

    /**
     * Descripción del estado
     */
    private String descripcion;

    /**
     * Cantidad de trámites en este estado
     */
    private Long cantidad;

    /**
     * Porcentaje del total (calculado en el servicio)
     */
    private Double porcentaje;
}
