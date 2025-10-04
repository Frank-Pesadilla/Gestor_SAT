package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.model.Cliente;
import com.gestor.gestor_sat.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query; 
import java.time.LocalDateTime;

@Repository
@Query("SELECT COUNT(c) FROM Cliente c WHERE c.createdAt BETWEEN :inicio AND :fin")
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // Para CU-SAT001
    boolean existsByDpi(String dpi);
    boolean existsByUsuario(Usuario usuario);
    Long countByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin); 
}