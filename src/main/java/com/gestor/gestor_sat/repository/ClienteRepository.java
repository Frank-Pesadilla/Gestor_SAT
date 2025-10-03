package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByDpi(String dpi);
    
    boolean existsByDpi(String dpi);
    
    Optional<Cliente> findByUsuarioIdUsuario(Long idUsuario);
    
    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.consultas WHERE c.idCliente = :id")
    Optional<Cliente> findByIdWithConsultas(Long id);
}