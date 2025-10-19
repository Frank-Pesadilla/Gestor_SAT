package com.gestor.gestor_sat.dto;

import com.gestor.gestor_sat.entity.enums.TipoNotificacion;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO para respuesta con información de la notificación
 * CU-SAT004: Generar Notificaciones
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionResponseDTO {

    private Long idNotificacion;
    private Long idUsuario;
    private String emailUsuario;
    private String mensaje;
    private TipoNotificacion tipo;
    private Boolean leida;
    private LocalDateTime fecha;
    private LocalDateTime fechaExpiracion;
    private Boolean expirada;
    private Long diasRestantes; // Días hasta que expire
}
