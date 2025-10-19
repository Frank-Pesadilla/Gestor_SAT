package com.gestor.gestor_sat.dto;

import com.gestor.gestor_sat.entity.enums.TipoNotificacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO para creación de notificaciones
 * CU-SAT004: Generar Notificaciones
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionCreateDTO {

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long idUsuario;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;

    @NotNull(message = "El tipo de notificación es obligatorio")
    private TipoNotificacion tipo;

    @Builder.Default
    private Boolean enviarEmail = false;
}
