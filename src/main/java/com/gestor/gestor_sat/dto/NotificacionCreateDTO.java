package com.gestor.gestor_sat.dto;

import com.gestor.gestor_sat.model.enums.TipoNotificacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionCreateDTO {
    
    @NotNull(message = "El ID de usuario es obligatorio")
    private Long idUsuario;
    
    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;
    
    @NotNull(message = "El tipo de notificación es obligatorio")
    private TipoNotificacion tipo;
    
    private Boolean enviarEmail = false;
}
