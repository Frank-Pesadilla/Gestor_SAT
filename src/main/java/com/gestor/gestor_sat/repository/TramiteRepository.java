package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.model.Tramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gestor.gestor_sat.model.enums.TramiteEstado; 
import org.springframework.data.jpa.repository.Query; 
import java.time.LocalDate; 
import java.util.List; 

@Repository
public interface TramiteRepository extends JpaRepository<Tramite, Long> {
    // Métodos básicos heredados de JpaRepository
    Long countByEstado(TramiteEstado estado); 
Long countByFechaCreacionBetween(LocalDate inicio, LocalDate fin); 
@Query("SELECT tt.nombre, COUNT(ct) " + 
"FROM ConsultaTramite ct " + 
"JOIN ct.tramite t " + 
"JOIN t.tipoTramite tt " + 
"GROUP BY tt.nombre " + 
"ORDER BY COUNT(ct) DESC") 
List<Object[]> findTop5TiposTramitesMasSolicitados(); 
}