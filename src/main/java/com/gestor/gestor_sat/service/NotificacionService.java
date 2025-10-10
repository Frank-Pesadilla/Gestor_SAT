package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.NotificacionResponseDTO;
import com.gestor.gestor_sat.entity.Notificacion;
import com.gestor.gestor_sat.entity.Usuario;
import com.gestor.gestor_sat.entity.enums.TipoNotificacion;
import com.gestor.gestor_sat.exception.CustomExceptions;
import com.gestor.gestor_sat.mapper.NotificacionMapper;
import com.gestor.gestor_sat.repository.NotificacionRepository;
import com.gestor.gestor_sat.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de notificaciones
 * CU-SAT004: Generar Notificaciones
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionMapper notificacionMapper;
    private final JavaMailSender mailSender;

    /**
     * Tarea 3: Crea una notificación para un usuario
     * @param idUsuario ID del usuario destinatario
     * @param mensaje Mensaje de la notificación
     * @param tipo Tipo de notificación
     * @return DTO con la notificación creada
     */
    @Transactional
    public NotificacionResponseDTO crearNotificacion(Long idUsuario, String mensaje, TipoNotificacion tipo) {
        log.info("Creando notificación para usuario ID: {} - Tipo: {}", idUsuario, tipo);

        // Validar que el usuario existe
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new CustomExceptions.UsuarioNoEncontradoException(idUsuario));

        // Tarea 7: Crear y guardar la notificación en BD
        Notificacion notificacion = Notificacion.builder()
                .usuario(usuario)
                .mensaje(mensaje)
                .tipo(tipo)
                .leida(false)
                .build();

        Notificacion notificacionGuardada = notificacionRepository.save(notificacion);
        log.info("Notificación creada exitosamente con ID: {}", notificacionGuardada.getIdNotificacion());

        return notificacionMapper.toResponseDTO(notificacionGuardada);
    }

    /**
     * Tarea 5: Envía una notificación por email usando JavaMailSender
     * @param idUsuario ID del usuario destinatario
     * @param mensaje Mensaje a enviar
     * @param tipo Tipo de notificación
     */
    public void enviarNotificacionPorEmail(Long idUsuario, String mensaje, TipoNotificacion tipo) {
        log.info("Enviando notificación por email a usuario ID: {}", idUsuario);

        // Validar que el usuario existe
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new CustomExceptions.UsuarioNoEncontradoException(idUsuario));

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(usuario.getEmail());
            mailMessage.setSubject(obtenerAsuntoEmail(tipo));
            mailMessage.setText(mensaje);
            mailMessage.setFrom("notificaciones@gestor-sat.com");

            mailSender.send(mailMessage);
            log.info("Email enviado exitosamente a: {}", usuario.getEmail());

        } catch (Exception e) {
            log.error("Error al enviar email a usuario ID: {} - Error: {}", idUsuario, e.getMessage());
            // No lanzamos excepción para que no falle el proceso principal
            // Solo registramos el error
        }
    }

    /**
     * Tarea 3 y 5: Crea una notificación y opcionalmente la envía por email
     * @param idUsuario ID del usuario destinatario
     * @param mensaje Mensaje de la notificación
     * @param tipo Tipo de notificación
     * @param enviarEmail Si debe enviarse por email
     * @return DTO con la notificación creada
     */
    @Transactional
    public NotificacionResponseDTO crearNotificacionConEmail(Long idUsuario, String mensaje, TipoNotificacion tipo, boolean enviarEmail) {
        // Crear la notificación en BD
        NotificacionResponseDTO notificacion = crearNotificacion(idUsuario, mensaje, tipo);

        // Enviar email si se solicita
        if (enviarEmail) {
            enviarNotificacionPorEmail(idUsuario, mensaje, tipo);
        }

        return notificacion;
    }

    /**
     * Tarea 8: Lista todas las notificaciones de un usuario
     * @param idUsuario ID del usuario
     * @return Lista de notificaciones
     */
    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> listarNotificacionesUsuario(Long idUsuario) {
        log.info("Listando notificaciones del usuario ID: {}", idUsuario);

        // Validar que el usuario existe
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new CustomExceptions.UsuarioNoEncontradoException(idUsuario);
        }

        List<Notificacion> notificaciones = notificacionRepository.findByUsuarioIdUsuarioOrderByFechaDesc(idUsuario);

        return notificaciones.stream()
                .map(notificacionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Tarea 9: Marca una notificación como leída
     * @param idNotificacion ID de la notificación
     * @return DTO con la notificación actualizada
     */
    @Transactional
    public NotificacionResponseDTO marcarComoLeida(Long idNotificacion) {
        log.info("Marcando notificación ID: {} como leída", idNotificacion);

        Notificacion notificacion = notificacionRepository.findById(idNotificacion)
                .orElseThrow(() -> new CustomExceptions.RecursoNoEncontradoException(
                        "Notificación", idNotificacion));

        notificacion.setLeida(true);
        Notificacion notificacionActualizada = notificacionRepository.save(notificacion);

        log.info("Notificación ID: {} marcada como leída exitosamente", idNotificacion);

        return notificacionMapper.toResponseDTO(notificacionActualizada);
    }

    /**
     * Lista notificaciones no leídas de un usuario
     */
    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> listarNotificacionesNoLeidas(Long idUsuario) {
        log.info("Listando notificaciones no leídas del usuario ID: {}", idUsuario);

        if (!usuarioRepository.existsById(idUsuario)) {
            throw new CustomExceptions.UsuarioNoEncontradoException(idUsuario);
        }

        List<Notificacion> notificaciones = notificacionRepository
                .findByUsuarioIdUsuarioAndLeidaFalseOrderByFechaDesc(idUsuario);

        return notificaciones.stream()
                .map(notificacionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Cuenta notificaciones no leídas de un usuario
     */
    @Transactional(readOnly = true)
    public Long contarNotificacionesNoLeidas(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new CustomExceptions.UsuarioNoEncontradoException(idUsuario);
        }

        return notificacionRepository.countByUsuarioIdUsuarioAndLeidaFalse(idUsuario);
    }

    /**
     * Obtiene el asunto del email según el tipo de notificación
     */
    private String obtenerAsuntoEmail(TipoNotificacion tipo) {
        return switch (tipo) {
            case INFO -> "[Gestor SAT] Información";
            case ADVERTENCIA -> "[Gestor SAT] Advertencia";
            case ERROR -> "[Gestor SAT] Error";
            case EXITO -> "[Gestor SAT] Operación Exitosa";
        };
    }
}
