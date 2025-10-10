package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
// Cambia la firma para extender JpaSpecificationExecutor
public interface ClienteRepository extends JpaRepository<Cliente, Long>, org.springframework.data.jpa.repository.JpaSpecificationExecutor<Cliente> {
    Optional<Cliente> findByDpi(String dpi);
    boolean existsByDpi(String dpi);
    boolean existsByUsuarioId(Long idUsuario);
    Optional<Cliente> findByUsuarioIdUsuario(Long idUsuario);
}

