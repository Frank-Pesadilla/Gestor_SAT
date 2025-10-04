package com.gestor.gestor_sat.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gestor.gestor_sat.model.ConsultaTramite;
import com.gestor.gestor_sat.model.enums.TramiteEstado;
<<<<<<< HEAD
<<<<<<< HEAD
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
=======

>>>>>>> Mafer
=======

>>>>>>> Mafer

@Repository
public interface ConsultaTramiteRepository extends JpaRepository<ConsultaTramite, Long> {
    
    List<ConsultaTramite> findByTramiteIdTramites(Long idTramite);
    
    List<ConsultaTramite> findByClienteIdCliente(Long idCliente);
    
    List<ConsultaTramite> findByEstado(TramiteEstado estado);
<<<<<<< HEAD
<<<<<<< HEAD
    
    Page<ConsultaTramite> findByClienteIdClienteOrderByFechaTramiteDesc(Long clienteId, Pageable pageable);
    
    // CU-SAT012 - Métodos para el Dashboard
    Long countByEstado(TramiteEstado estado);
    
    Long countByFechaTramiteBetween(LocalDate inicio, LocalDate fin);
    
    @Query("SELECT tt.nombre, COUNT(ct) " +
           "FROM ConsultaTramite ct " +
           "JOIN ct.tramite t " +
           "JOIN t.tipoTramite tt " +
           "GROUP BY tt.nombre " +
           "ORDER BY COUNT(ct) DESC")
    List<Object[]> findTop5TiposTramitesMasSolicitados();
=======
=======
>>>>>>> Mafer

    Long countByEstado(TramiteEstado estado); 
Long countByFechaCreacionBetween(LocalDate inicio, LocalDate fin); 
@Query("SELECT tt.nombre, COUNT(ct) " + 
"FROM ConsultaTramite ct " + 
"JOIN ct.tramite t " + 
"JOIN t.tipoTramite tt " + 
"GROUP BY tt.nombre " + 
"ORDER BY COUNT(ct) DESC") 
List<Object[]> findTop5TiposTramitesMasSolicitados();
<<<<<<< HEAD
>>>>>>> Mafer
=======
>>>>>>> Mafer
}