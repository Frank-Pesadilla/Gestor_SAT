package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.entity.ConsultaTramite;
import com.gestor.gestor_sat.entity.enums.TramiteEstado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConsultaTramiteRepository extends JpaRepository<ConsultaTramite, Long> {
    
    List<ConsultaTramite> findByClienteIdCliente(Long idCliente);
    
    List<ConsultaTramite> findByTramiteIdTramites(Long idTramites);
    
    List<ConsultaTramite> findByEstado(TramiteEstado estado);
    
    @Query("SELECT ct FROM ConsultaTramite ct WHERE ct.cliente.idCliente = :idCliente ORDER BY ct.fechaTramite DESC")
    List<ConsultaTramite> findByClienteOrderByFechaDesc(Long idCliente);
    
    @Query("SELECT ct FROM ConsultaTramite ct WHERE ct.fechaTramite BETWEEN :fechaInicio AND :fechaFin")
    List<ConsultaTramite> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    long countByEstado(TramiteEstado estado);
    
    @Query("SELECT COUNT(ct) FROM ConsultaTramite ct WHERE MONTH(ct.fechaTramite) = :mes AND YEAR(ct.fechaTramite) = :anio")
    long countByMesAndAnio(int mes, int anio);

    /**
     * CU-SAT005: Historial de Trámites - Tarea 3 y Tarea 7
     * Obtiene el historial de trámites de un cliente con paginación
     * Ordenado por fecha descendente (más recientes primero) - Tarea 5
     *
     * @param idCliente ID del cliente
     * @param pageable Configuración de paginación y ordenamiento
     * @return Página de consultas de trámite
     */
    @Query("SELECT ct FROM ConsultaTramite ct WHERE ct.cliente.idCliente = :idCliente ORDER BY ct.fechaTramite DESC")
    Page<ConsultaTramite> findByClienteIdClienteWithPagination(Long idCliente, Pageable pageable);

    /**
     * CU-SAT012: Panel de Control - Tarea 6 y Tarea 7
     * Obtiene top N tipos de trámites más solicitados
     * Agrupa por tipo de trámite y cuenta las solicitudes
     *
     * @param pageable Configuración de paginación (usar PageRequest.of(0, 5) para top 5)
     * @return Lista de arrays [idTipoTramite, portal, cantidad]
     */
    @Query("""
        SELECT tt.idTipoTramite, tt.portal, COUNT(ct) as cantidad
        FROM ConsultaTramite ct
        JOIN ct.tramite t
        JOIN t.tipoTramite tt
        GROUP BY tt.idTipoTramite, tt.portal
        ORDER BY cantidad DESC
        """)
    List<Object[]> findTopTiposTramites(Pageable pageable);

    /**
     * CU-SAT012: Panel de Control - Tarea 5 y Tarea 7
     * Cuenta trámites creados en un rango de fechas
     * Usado para calcular trámites del mes actual
     *
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Cantidad de trámites en el rango
     */
    @Query("SELECT COUNT(ct) FROM ConsultaTramite ct WHERE ct.fechaTramite BETWEEN :fechaInicio AND :fechaFin")
    long countByFechaTramiteBetween(LocalDate fechaInicio, LocalDate fechaFin);
}