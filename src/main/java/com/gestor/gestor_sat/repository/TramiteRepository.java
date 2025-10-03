package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.model.Tramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TramiteRepository extends JpaRepository<Tramite, Long> {
    
    List<Tramite> findByTipoTramiteIdTipoTramite(Long idTipoTramite);
    
    @Query("SELECT t FROM Tramite t LEFT JOIN FETCH t.consultas WHERE t.idTramites = :id")
    Tramite findByIdWithConsultas(Long id);
    
    @Query("SELECT t FROM Tramite t LEFT JOIN FETCH t.archivos WHERE t.idTramites = :id")
    Tramite findByIdWithArchivos(Long id);
}