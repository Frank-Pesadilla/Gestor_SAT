package com.gestor.gestor_sat.dto;

import lombok.*;

/**
 * DTO para top tipos de trámites más solicitados
 * CU-SAT012: Panel de Control - Tarea 6
 *
 * Representa un tipo de trámite con su cantidad de solicitudes
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopTipoTramiteDTO {

    /**
     * ID del tipo de trámite
     */
    private Long idTipoTramite;

    /**
     * Portal del tipo de trámite (SAT, RENAP, IGSS, etc.)
     */
    private String portal;

    /**
     * Nombre del trámite más común de este tipo
     */
    private String nombreTramite;

    /**
     * Cantidad de veces solicitado
     */
    private Long cantidad;

    /**
     * Posición en el ranking (1-5)
     */
    private Integer posicion;
}
