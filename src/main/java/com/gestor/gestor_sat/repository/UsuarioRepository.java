package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.model.Usuario;
import com.gestor.gestor_sat.model.enums.UsuarioEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    Optional<Usuario> findByUsuario(String usuario);
    
    boolean existsByEmail(String email);
    
    boolean existsByUsuario(String usuario);
    
    Optional<Usuario> findByEmailAndEstado(String email, UsuarioEstado estado);
}