package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByDpi(String dpi);
    
    boolean existsByDpi(String dpi);
    
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cliente c WHERE c.usuario.idUsuario = :idUsuario")
    boolean existsByUsuarioId(Long idUsuario);
    
    Optional<Cliente> findByUsuarioIdUsuario(Long idUsuario);
}