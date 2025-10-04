package com.gestor.gestor_sat.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gestor.gestor_sat.model.Bitacora;
import com.gestor.gestor_sat.model.Usuario;

@Repository
public interface BitacoraRepository extends JpaRepository<Bitacora, Long> {
    
    Page<Bitacora> findByUsuario(Usuario usuario, Pageable pageable);
    
    Page<Bitacora> findByAccion(String accion, Pageable pageable);
    
    @Query("SELECT b FROM Bitacora b WHERE b.createdAt BETWEEN :inicio AND :fin")
    Page<Bitacora> findByFechaRango(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
    
    @Query("SELECT b FROM Bitacora b WHERE " +
           "(:usuario IS NULL OR b.usuario = :usuario) AND " +
           "(:accion IS NULL OR b.accion = :accion) AND " +
           "(:fechaInicio IS NULL OR b.createdAt >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR b.createdAt <= :fechaFin)")
    Page<Bitacora> buscarConFiltros(Usuario usuario, String accion, 
                                     LocalDateTime fechaInicio, LocalDateTime fechaFin, 
                                     Pageable pageable);
}
