package com.gestor.gestor_sat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO para restablecer contraseña con token
 * CU-SAT013 - Tarea 11
 *
 * Usado en el endpoint POST /api/auth/restablecer-contrasena
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestablecerContrasenaRequestDTO {

    /**
     * Token UUID recibido por email
     */
    @NotBlank(message = "El token es obligatorio")
    private String token;

    /**
     * Nueva contraseña del usuario
     * Mínimo 8 caracteres por seguridad
     */
    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String nuevaContrasena;

    /**
     * Convierte el token String a UUID
     * @return UUID del token
     * @throws IllegalArgumentException si el token no es un UUID válido
     */
    public UUID getTokenAsUUID() {
        try {
            return UUID.fromString(token);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El token no tiene un formato válido", e);
        }
    }
}
