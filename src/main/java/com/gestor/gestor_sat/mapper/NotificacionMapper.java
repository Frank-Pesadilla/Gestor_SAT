package com.gestor.gestor_sat.mapper;

import com.gestor.gestor_sat.dto.NotificacionResponseDTO;
import com.gestor.gestor_sat.entity.Notificacion;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
        Long diasRestantes = calcularDiasRestantes(notificacion.getFechaExpiracion());

        return NotificacionResponseDTO.builder()
                .idNotificacion(notificacion.getIdNotificacion())
                .idUsuario(notificacion.getUsuario().getIdUsuario())
                .emailUsuario(notificacion.getUsuario().getEmail())
                .mensaje(notificacion.getMensaje())
                .tipo(notificacion.getTipo())
                .leida(notificacion.getLeida())
                .fecha(notificacion.getFecha())
                .fechaExpiracion(notificacion.getFechaExpiracion())
                .expirada(notificacion.getExpirada())
                .diasRestantes(diasRestantes)
                .build();
    }

    /**
     * Calcula los días restantes hasta la expiración
     */
    private Long calcularDiasRestantes(LocalDateTime fechaExpiracion) {
        LocalDateTime ahora = LocalDateTime.now();
        long dias = ChronoUnit.DAYS.between(ahora, fechaExpiracion);
        return dias < 0 ? 0 : dias; // Si ya expiró, retorna 0
    }
}
