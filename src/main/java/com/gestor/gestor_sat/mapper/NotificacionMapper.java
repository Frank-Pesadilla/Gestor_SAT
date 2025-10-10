package com.gestor.gestor_sat.mapper;

import com.gestor.gestor_sat.dto.NotificacionResponseDTO;
import com.gestor.gestor_sat.entity.Notificacion;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre Notificacion entity y DTOs
 * CU-SAT004: Generar Notificaciones
 */
@Component
public class NotificacionMapper {

    /**
     * Convierte entidad Notificacion a NotificacionResponseDTO
     */
    public NotificacionResponseDTO toResponseDTO(Notificacion notificacion) {
        return NotificacionResponseDTO.builder()
                .idNotificacion(notificacion.getIdNotificacion())
                .idUsuario(notificacion.getUsuario().getIdUsuario())
                .emailUsuario(notificacion.getUsuario().getEmail())
                .mensaje(notificacion.getMensaje())
                .tipo(notificacion.getTipo())
                .leida(notificacion.getLeida())
                .fecha(notificacion.getFecha())
                .build();
    }
}
