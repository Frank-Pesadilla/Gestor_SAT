package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.model.PasswordResetToken;
import com.gestor.gestor_sat.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    
    Optional<PasswordResetToken> findByToken(UUID token);
    
    void deleteByUsuario(Usuario usuario);
}