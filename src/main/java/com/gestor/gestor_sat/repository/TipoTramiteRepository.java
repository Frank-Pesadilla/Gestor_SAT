package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.model.TipoTramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoTramiteRepository extends JpaRepository<TipoTramite, Long> {
    // Métodos básicos heredados de JpaRepository
}