package com.gestor.gestor_sat.dto;

import lombok.*;

/**
 * DTO para datos de gráfica de trámites por mes
 * CU-SAT012: Panel de Control - Tarea 9
 *
 * Estructura lista para gráficas de línea o barras en el frontend
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TramitePorMesDTO {

    /**
     * Nombre del mes (ej: "Enero", "Febrero", etc.)
     */
    private String mes;

    /**
     * Número del mes (1-12)
     */
    private Integer numeroMes;

    /**
     * Año
     */
    private Integer anio;

    /**
     * Cantidad de trámites en ese mes
     */
    private Long cantidad;

    /**
     * Etiqueta para gráfica (ej: "Ene 2025")
     */
    private String label;
}
