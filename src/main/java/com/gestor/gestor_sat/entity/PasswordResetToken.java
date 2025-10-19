package com.gestor.gestor_sat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "password_reset_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_password_reset_token")
    private UUID idPasswordResetToken;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "token", nullable = false, unique = true)
    @Builder.Default
    private UUID token = UUID.randomUUID();

    @Column(name = "expires_at", nullable = false)
    private ZonedDateTime expiresAt;

    @Column(name = "used_at")
    private ZonedDateTime usedAt;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private ZonedDateTime createdAt = ZonedDateTime.now();

    /**
     * Verifica si el token ha expirado
     */
    public boolean isExpired() {
        return ZonedDateTime.now().isAfter(expiresAt);
    }

    /**
     * Verifica si el token ya fue usado
     */
    public boolean isUsed() {
        return usedAt != null;
    }

    /**
     * Verifica si el token es válido (no expirado y no usado)
     */
    public boolean isValid() {
        return !isExpired() && !isUsed();
    }
}