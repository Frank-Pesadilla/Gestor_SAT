package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.entity.ConsultaTramite;
import com.gestor.gestor_sat.entity.enums.TramiteEstado;
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
}