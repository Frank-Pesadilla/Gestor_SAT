package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.model.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchivoRepository extends JpaRepository<Archivo, Long> {
    
    List<Archivo> findByTramiteIdTramites(Long idTramite);
    
    List<Archivo> findByClienteIdCliente(Long idCliente);
}