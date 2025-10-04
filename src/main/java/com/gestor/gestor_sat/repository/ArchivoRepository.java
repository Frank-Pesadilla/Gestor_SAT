package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.model.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivoRepository extends JpaRepository<Archivo, Long> {
    // Métodos básicos heredados
}