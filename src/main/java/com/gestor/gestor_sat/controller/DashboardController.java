package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.dto.EstadisticasDTO;
import com.gestor.gestor_sat.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para el Panel de Control (Dashboard)
 * CU-SAT012: Panel de Control - Tarea 8
 *
 * Proporciona endpoints para obtener estadísticas y métricas del sistema
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * CU-SAT012 - Tarea 8: Endpoint GET para obtener todas las estadísticas
     *
     * Retorna un DTO completo con:
     * - Total de clientes (Tarea 3)
     * - Total de trámites
     * - Trámites por estado (Tarea 4) - INICIADO, PENDIENTE, TERMINADO
     * - Trámites del mes actual (Tarea 5)
     * - Clientes nuevos del mes (Tarea 5)
     * - Top 5 tipos de trámites más solicitados (Tarea 6)
     * - Datos para gráficas (Tarea 9):
     *   * Trámites por mes (últimos 12 meses)
     *   * Clientes por mes (últimos 12 meses)
     *   * Trámites por estado con porcentajes
     *
     * FORMATO DE RESPUESTA:
     * El DTO incluye dos formatos de datos para gráficas:
     * 1. Detallado: Listas de objetos con toda la información
     * 2. Simplificado: Formato { "labels": [...], "data": [...] } listo para usar
     *
     * @return ResponseEntity con EstadisticasDTO
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasDTO> obtenerEstadisticas() {
        log.info("GET /api/dashboard/estadisticas - Obteniendo estadísticas del sistema");

        // Calcular todas las estadísticas
        EstadisticasDTO estadisticas = dashboardService.obtenerEstadisticas();

        log.info("Estadísticas obtenidas exitosamente - Total clientes: {}, Total trámites: {}",
                estadisticas.getTotalClientes(), estadisticas.getTotalTramites());

        return ResponseEntity.ok(estadisticas);
    }
}
