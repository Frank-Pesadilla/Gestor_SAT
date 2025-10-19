package com.gestor.gestor_sat.dto;

import com.gestor.gestor_sat.entity.enums.UsuarioEstado;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO para respuesta de usuario (sin contraseña)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private Long idUsuario;
    private String usuario;
    private String email;
    private UsuarioEstado estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}