package com.gestor.gestor_sat.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "password_reset_token", indexes = {
    @Index(name = "idx_password_reset_usuario", columnList = "id_usuario"),
    @Index(name = "idx_password_reset_token", columnList = "token"),
    @Index(name = "idx_password_reset_expires", columnList = "expires_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {
    
    @Id
    @Column(name = "id_password_reset_token")
    @Builder.Default
    private UUID idPasswordResetToken = UUID.randomUUID();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    @Column(nullable = false, unique = true)
    @Builder.Default
    private UUID token = UUID.randomUUID();
    
    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;
    
    @Column(name = "used_at")
    private OffsetDateTime usedAt;
    
    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private OffsetDateTime createdAt = OffsetDateTime.now();
    
    // Métodos de utilidad
    public boolean isExpired() {
        return OffsetDateTime.now().isAfter(expiresAt);
    }
    
    public boolean isUsed() {
        return usedAt != null;
    }
}