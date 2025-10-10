package com.gestor.gestor_sat.dto;

import lombok.*;

import java.util.List;

/**
 * DTO principal con todas las estadísticas del dashboard
 * CU-SAT012: Panel de Control - Tarea 1
 *
 * Contiene todas las métricas del sistema:
 * - Totales generales
 * - Trámites por estado
 * - Datos para gráficas
 * - Top rankings
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadisticasDTO {

    // ===============================================================
    // MÉTRICAS GENERALES
    // ===============================================================

    /**
     * Tarea 3: Total de clientes en el sistema
     */
    private Long totalClientes;

    /**
     * Total de trámites (ConsultaTramite) en el sistema
     */
    private Long totalTramites;

    /**
     * Total de tipos de trámites disponibles
     */
    private Long totalTiposTramites;

    /**
     * Clientes nuevos registrados en el mes actual
     * Tarea 5: Filtrado por fecha de creación dentro del mes en curso
     */
    private Long clientesNuevosDelMes;

    /**
     * Trámites creados en el mes actual
     * Tarea 5: Filtrado por fecha dentro del mes en curso
     */
    private Long tramitesDelMes;

    // ===============================================================
    // TRÁMITES POR ESTADO
    // ===============================================================
    // Tarea 4: Conteo de trámites por cada estado del sistema

    /**
     * Trámites en estado INICIADO
     */
    private Long tramitesIniciados;

    /**
     * Trámites en estado PENDIENTE
     */
    private Long tramitesPendientes;

    /**
     * Trámites en estado TERMINADO
     */
    private Long tramitesTerminados;

    // ===============================================================
    // TOP RANKINGS
    // ===============================================================

    /**
     * Tarea 6: Top 5 tipos de trámites más solicitados
     * Ordenados por cantidad descendente
     */
    private List<TopTipoTramiteDTO> topTiposTramites;

    // ===============================================================
    // DATOS PARA GRÁFICAS
    // ===============================================================
    // Tarea 9: Datos estructurados listos para visualización

    /**
     * Trámites por estado para gráfica de pastel/dona
     * Incluye estado, cantidad y porcentaje
     */
    private List<TramitePorEstadoDTO> tramitesPorEstado;

    /**
     * Trámites por mes (últimos 12 meses) para gráfica de línea/barras
     * Ordenados cronológicamente
     */
    private List<TramitePorMesDTO> tramitesPorMes;

    /**
     * Clientes nuevos por mes (últimos 12 meses) para gráfica de línea
     * Ordenados cronológicamente
     */
    private List<TramitePorMesDTO> clientesPorMes;

    // ===============================================================
    // DATOS PARA GRÁFICAS (FORMATO SIMPLIFICADO)
    // ===============================================================
    // Tarea 9: Estructura alternativa { "labels": [...], "data": [...] }

    /**
     * Datos simplificados para gráfica de trámites por mes
     */
    private GraficaDataDTO graficaTramitesMes;

    /**
     * Datos simplificados para gráfica de clientes nuevos
     */
    private GraficaDataDTO graficaClientesMes;

    /**
     * DTO interno para formato simplificado de gráficas
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GraficaDataDTO {
        /**
         * Etiquetas para el eje X
         */
        private List<String> labels;

        /**
         * Valores para el eje Y
         */
        private List<Long> data;
    }
}
