package com.gestor.gestor_sat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
<<<<<<< HEAD
<<<<<<< HEAD
import java.util.Optional;
=======

import com.gestor.gestor_sat.model.Datos;
>>>>>>> Mafer
=======

import com.gestor.gestor_sat.model.Datos;
>>>>>>> Mafer

@Repository
public interface DatosRepository extends JpaRepository<Datos, Long> {
    
    Optional<Datos> findByClienteIdCliente(Long idCliente);
    
    Optional<Datos> findByNit(String nit);
    Optional<Datos> findByClienteId(Long clienteId); 
<<<<<<< HEAD
<<<<<<< HEAD
=======
    
>>>>>>> Mafer
=======
    
>>>>>>> Mafer
}