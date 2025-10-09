package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.entity.Tramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TramiteRepository extends JpaRepository<Tramite, Long> {
    
    List<Tramite> findByTipoTramiteIdTipoTramite(Long idTipoTramite);
    
    @Query("SELECT t FROM Tramite t WHERE LOWER(t.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Tramite> findByNombreContainingIgnoreCase(String nombre);
}