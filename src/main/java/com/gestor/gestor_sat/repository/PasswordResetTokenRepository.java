package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    
    Optional<PasswordResetToken> findByToken(UUID token);
    
    List<PasswordResetToken> findByUsuarioIdUsuario(Long idUsuario);
    
    @Query("SELECT t FROM PasswordResetToken t WHERE t.expiresAt < :now AND t.usedAt IS NULL")
    List<PasswordResetToken> findExpiredTokens(ZonedDateTime now);
    
    void deleteByUsuarioIdUsuario(Long idUsuario);
}