package com.gestor.gestor_sat.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gestor.gestor_sat.model.Cliente; 
import com.gestor.gestor_sat.model.Usuario; 


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // Para CU-SAT001
    boolean existsByDpi(String dpi);
    boolean existsByUsuario(Usuario usuario);
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.createdAt BETWEEN :inicio AND :fin") 
Long countByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);
}