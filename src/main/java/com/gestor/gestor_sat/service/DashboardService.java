package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.EstadisticasDTO;
import com.gestor.gestor_sat.dto.TopTipoTramiteDTO;
import com.gestor.gestor_sat.dto.TramitePorEstadoDTO;
import com.gestor.gestor_sat.dto.TramitePorMesDTO;
import com.gestor.gestor_sat.entity.enums.TramiteEstado;
import com.gestor.gestor_sat.repository.ClienteRepository;
import com.gestor.gestor_sat.repository.ConsultaTramiteRepository;
import com.gestor.gestor_sat.repository.TipoTramiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Servicio para el Panel de Control (Dashboard)
 * CU-SAT012: Panel de Control - Tarea 2
 *
 * Calcula y proporciona todas las estadísticas y métricas del sistema:
 * - Totales generales
 * - Trámites por estado
 * - Datos para gráficas
 * - Top rankings
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final ClienteRepository clienteRepository;
    private final ConsultaTramiteRepository consultaTramiteRepository;
    private final TipoTramiteRepository tipoTramiteRepository;

    /**
     * CU-SAT012 - Tarea 2: Obtiene todas las estadísticas del sistema
     *
     * MÉTRICAS CALCULADAS:
     * - Tarea 3: Total de clientes
     * - Tarea 4: Trámites por estado (INICIADO, PENDIENTE, TERMINADO)
     * - Tarea 5: Trámites y clientes del mes actual
     * - Tarea 6: Top 5 tipos de trámites más solicitados
     * - Tarea 9: Datos para gráficas (por mes, por estado)
     *
     * @return DTO con todas las estadísticas
     */
    @Transactional(readOnly = true)
    public EstadisticasDTO obtenerEstadisticas() {
        log.info("Calculando estadísticas del dashboard");

        // Calcular mes actual
        LocalDate hoy = LocalDate.now();
        LocalDate primerDiaMes = hoy.withDayOfMonth(1);
        LocalDateTime inicioMes = primerDiaMes.atStartOfDay();
        LocalDateTime finMes = hoy.atTime(23, 59, 59);

        // TAREA 3: Calcular total de clientes
        long totalClientes = clienteRepository.count();
        log.debug("Total de clientes: {}", totalClientes);

        // Calcular total de trámites
        long totalTramites = consultaTramiteRepository.count();
        log.debug("Total de trámites: {}", totalTramites);

        // Calcular total de tipos de trámites
        long totalTiposTramites = tipoTramiteRepository.count();

        // TAREA 5: Calcular clientes nuevos del mes actual
        long clientesNuevosDelMes = clienteRepository.countByCreatedAtBetween(inicioMes, finMes);
        log.debug("Clientes nuevos del mes: {}", clientesNuevosDelMes);

        // TAREA 5: Calcular trámites del mes actual
        long tramitesDelMes = consultaTramiteRepository.countByFechaTramiteBetween(primerDiaMes, hoy);
        log.debug("Trámites del mes: {}", tramitesDelMes);

        // TAREA 4: Calcular trámites por cada estado
        long tramitesIniciados = consultaTramiteRepository.countByEstado(TramiteEstado.INICIADO);
        long tramitesPendientes = consultaTramiteRepository.countByEstado(TramiteEstado.PENDIENTE);
        long tramitesTerminados = consultaTramiteRepository.countByEstado(TramiteEstado.TERMINADO);

        log.debug("Trámites por estado - Iniciados: {}, Pendientes: {}, Terminados: {}",
                tramitesIniciados, tramitesPendientes, tramitesTerminados);

        // TAREA 6: Top 5 tipos de trámites más solicitados
        List<TopTipoTramiteDTO> topTiposTramites = obtenerTopTiposTramites();

        // TAREA 9: Datos para gráficas
        List<TramitePorEstadoDTO> tramitesPorEstado = obtenerTramitesPorEstado(
                tramitesIniciados, tramitesPendientes, tramitesTerminados, totalTramites);

        List<TramitePorMesDTO> tramitesPorMes = obtenerTramitesPorMes();
        List<TramitePorMesDTO> clientesPorMes = obtenerClientesPorMes();

        // Construir DTO principal
        EstadisticasDTO estadisticas = EstadisticasDTO.builder()
                // Métricas generales
                .totalClientes(totalClientes)
                .totalTramites(totalTramites)
                .totalTiposTramites(totalTiposTramites)
                .clientesNuevosDelMes(clientesNuevosDelMes)
                .tramitesDelMes(tramitesDelMes)

                // Trámites por estado
                .tramitesIniciados(tramitesIniciados)
                .tramitesPendientes(tramitesPendientes)
                .tramitesTerminados(tramitesTerminados)

                // Top rankings
                .topTiposTramites(topTiposTramites)

                // Datos para gráficas
                .tramitesPorEstado(tramitesPorEstado)
                .tramitesPorMes(tramitesPorMes)
                .clientesPorMes(clientesPorMes)

                // Datos simplificados para gráficas
                .graficaTramitesMes(convertirAGraficaData(tramitesPorMes))
                .graficaClientesMes(convertirAGraficaData(clientesPorMes))
                .build();

        log.info("Estadísticas calculadas exitosamente");

        return estadisticas;
    }

    /**
     * TAREA 6: Obtiene top 5 tipos de trámites más solicitados
     * Agrupa por tipo de trámite y ordena por cantidad descendente
     *
     * @return Lista de top 5 tipos de trámites
     */
    private List<TopTipoTramiteDTO> obtenerTopTiposTramites() {
        log.debug("Calculando top 5 tipos de trámites");

        // Usar PageRequest.of(0, 5) para obtener solo los primeros 5 resultados
        List<Object[]> results = consultaTramiteRepository.findTopTiposTramites(PageRequest.of(0, 5));

        List<TopTipoTramiteDTO> topTipos = new ArrayList<>();
        int posicion = 1;

        for (Object[] row : results) {
            Long idTipoTramite = ((Number) row[0]).longValue();
            String portal = (String) row[1];
            Long cantidad = ((Number) row[2]).longValue();

            topTipos.add(TopTipoTramiteDTO.builder()
                    .idTipoTramite(idTipoTramite)
                    .portal(portal)
                    .nombreTramite(portal) // Simplificado, usar portal como nombre
                    .cantidad(cantidad)
                    .posicion(posicion++)
                    .build());
        }

        log.debug("Top 5 tipos de trámites calculado: {} resultados", topTipos.size());

        return topTipos;
    }

    /**
     * TAREA 9: Obtiene trámites agrupados por estado para gráfica
     * Incluye porcentaje de cada estado
     *
     * @param iniciados Cantidad de trámites iniciados
     * @param pendientes Cantidad de trámites pendientes
     * @param terminados Cantidad de trámites terminados
     * @param total Total de trámites
     * @return Lista de trámites por estado con porcentajes
     */
    private List<TramitePorEstadoDTO> obtenerTramitesPorEstado(
            long iniciados, long pendientes, long terminados, long total) {

        List<TramitePorEstadoDTO> lista = new ArrayList<>();

        // Evitar división por cero
        double totalDouble = total > 0 ? total : 1.0;

        lista.add(TramitePorEstadoDTO.builder()
                .estado(TramiteEstado.INICIADO)
                .descripcion(TramiteEstado.INICIADO.getDescripcion())
                .cantidad(iniciados)
                .porcentaje((iniciados / totalDouble) * 100)
                .build());

        lista.add(TramitePorEstadoDTO.builder()
                .estado(TramiteEstado.PENDIENTE)
                .descripcion(TramiteEstado.PENDIENTE.getDescripcion())
                .cantidad(pendientes)
                .porcentaje((pendientes / totalDouble) * 100)
                .build());

        lista.add(TramitePorEstadoDTO.builder()
                .estado(TramiteEstado.TERMINADO)
                .descripcion(TramiteEstado.TERMINADO.getDescripcion())
                .cantidad(terminados)
                .porcentaje((terminados / totalDouble) * 100)
                .build());

        return lista;
    }

    /**
     * TAREA 9: Obtiene trámites de los últimos 12 meses para gráfica de línea
     * Ordenados cronológicamente (del más antiguo al más reciente)
     *
     * @return Lista de trámites por mes
     */
    private List<TramitePorMesDTO> obtenerTramitesPorMes() {
        log.debug("Calculando trámites por mes (últimos 12 meses)");

        List<TramitePorMesDTO> lista = new ArrayList<>();
        YearMonth mesActual = YearMonth.now();

        // Iterar sobre los últimos 12 meses
        for (int i = 11; i >= 0; i--) {
            YearMonth mes = mesActual.minusMonths(i);
            int numeroMes = mes.getMonthValue();
            int anio = mes.getYear();

            // Obtener cantidad de trámites de ese mes
            long cantidad = consultaTramiteRepository.countByMesAndAnio(numeroMes, anio);

            // Nombre del mes en español
            String nombreMes = mes.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
            String label = nombreMes.substring(0, 3) + " " + anio; // Ej: "Ene 2025"

            lista.add(TramitePorMesDTO.builder()
                    .mes(nombreMes)
                    .numeroMes(numeroMes)
                    .anio(anio)
                    .cantidad(cantidad)
                    .label(label)
                    .build());
        }

        log.debug("Trámites por mes calculados: {} meses", lista.size());

        return lista;
    }

    /**
     * TAREA 9: Obtiene clientes nuevos de los últimos 12 meses para gráfica
     * Ordenados cronológicamente
     *
     * @return Lista de clientes nuevos por mes
     */
    private List<TramitePorMesDTO> obtenerClientesPorMes() {
        log.debug("Calculando clientes nuevos por mes (últimos 12 meses)");

        List<TramitePorMesDTO> lista = new ArrayList<>();
        YearMonth mesActual = YearMonth.now();

        // Iterar sobre los últimos 12 meses
        for (int i = 11; i >= 0; i--) {
            YearMonth mes = mesActual.minusMonths(i);
            int numeroMes = mes.getMonthValue();
            int anio = mes.getYear();

            // Obtener cantidad de clientes nuevos de ese mes
            long cantidad = clienteRepository.countByMesAndAnio(numeroMes, anio);

            // Nombre del mes en español
            String nombreMes = mes.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
            String label = nombreMes.substring(0, 3) + " " + anio; // Ej: "Ene 2025"

            lista.add(TramitePorMesDTO.builder()
                    .mes(nombreMes)
                    .numeroMes(numeroMes)
                    .anio(anio)
                    .cantidad(cantidad)
                    .label(label)
                    .build());
        }

        log.debug("Clientes por mes calculados: {} meses", lista.size());

        return lista;
    }

    /**
     * TAREA 9: Convierte lista de TramitePorMesDTO a formato simplificado de gráfica
     * Estructura: { "labels": [...], "data": [...] }
     *
     * @param datosMes Lista de datos por mes
     * @return Datos en formato simplificado para gráficas
     */
    private EstadisticasDTO.GraficaDataDTO convertirAGraficaData(List<TramitePorMesDTO> datosMes) {
        List<String> labels = datosMes.stream()
                .map(TramitePorMesDTO::getLabel)
                .collect(Collectors.toList());

        List<Long> data = datosMes.stream()
                .map(TramitePorMesDTO::getCantidad)
                .collect(Collectors.toList());

        return EstadisticasDTO.GraficaDataDTO.builder()
                .labels(labels)
                .data(data)
                .build();
    }
}
