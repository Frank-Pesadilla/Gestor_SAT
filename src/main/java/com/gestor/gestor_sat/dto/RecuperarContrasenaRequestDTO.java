package com.gestor.gestor_sat.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitud de recuperación de contraseña
 * CU-SAT013 - Tarea 10
 *
 * Usado en el endpoint POST /api/auth/recuperar-contrasena
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecuperarContrasenaRequestDTO {

    /**
     * Email del usuario que solicita recuperar su contraseña
     */
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe proporcionar un email válido")
    private String email;
}
