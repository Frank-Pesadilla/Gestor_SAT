package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.model.Cliente;
import com.gestor.gestor_sat.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // Para CU-SAT001
    boolean existsByDpi(String dpi);
    boolean existsByUsuario(Usuario usuario);
}