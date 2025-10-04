package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.model.Tramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TramiteRepository extends JpaRepository<Tramite, Long> {
    // Métodos básicos heredados de JpaRepository
}