package com.gestor.gestor_sat.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
=======
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gestor.gestor_sat.model.Cliente; 
import com.gestor.gestor_sat.model.Usuario; 

>>>>>>> Mafer
=======
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gestor.gestor_sat.model.Cliente; 
import com.gestor.gestor_sat.model.Usuario; 

>>>>>>> Mafer

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>, 
                                          JpaSpecificationExecutor<Cliente> {
    
    boolean existsByDpi(String dpi);
    boolean existsByUsuario(Usuario usuario);
<<<<<<< HEAD
<<<<<<< HEAD
    
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.createdAt BETWEEN :inicio AND :fin")
    Long countByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);
=======
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.createdAt BETWEEN :inicio AND :fin") 
Long countByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);
>>>>>>> Mafer
=======
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.createdAt BETWEEN :inicio AND :fin") 
Long countByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);
>>>>>>> Mafer
}