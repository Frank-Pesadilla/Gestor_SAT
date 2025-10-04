package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.model.ConsultaTramite;
import com.gestor.gestor_sat.model.enums.TramiteEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 

import java.util.List;

@Repository
public interface ConsultaTramiteRepository extends JpaRepository<ConsultaTramite, Long> {
    
    List<ConsultaTramite> findByTramiteIdTramites(Long idTramite);
    
    List<ConsultaTramite> findByClienteIdCliente(Long idCliente);
    
    List<ConsultaTramite> findByEstado(TramiteEstado estado);

    
    long countByEstado(TramiteEstado estado);
    Page<ConsultaTramite> findByClienteIdOrderByFechaCreacionDesc(Long clienteId, Pageable 
pageable); 
}