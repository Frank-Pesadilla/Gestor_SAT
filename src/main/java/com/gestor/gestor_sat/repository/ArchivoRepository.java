package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.entity.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchivoRepository extends JpaRepository<Archivo, Long> {
    
    List<Archivo> findByClienteIdCliente(Long idCliente);
    
    List<Archivo> findByTramiteIdTramites(Long idTramites);
    
    List<Archivo> findByClienteIdClienteAndTramiteIdTramites(Long idCliente, Long idTramites);
}