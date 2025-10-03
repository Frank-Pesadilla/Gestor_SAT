package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.model.Datos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DatosRepository extends JpaRepository<Datos, Long> {
    
    Optional<Datos> findByClienteIdCliente(Long idCliente);
    
    Optional<Datos> findByNit(String nit);
}