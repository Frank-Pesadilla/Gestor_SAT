package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.NotificacionCreateDTO;
import com.gestor.gestor_sat.dto.NotificacionDTO;
import com.gestor.gestor_sat.model.Notificacion;
import com.gestor.gestor_sat.model.Usuario;
import com.gestor.gestor_sat.model.enums.TipoNotificacion;
import com.gestor.gestor_sat.repository.NotificacionRepository;
import com.gestor.gestor_sat.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionService {
    
    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final JavaMailSender mailSender;
    
    /**
     * Crea una nueva notificación
     */
    @Transactional
    public NotificacionDTO crearNotificacion(Long idUsuario, String mensaje, 
                                            TipoNotificacion tipo) {
        log.info("Creando notificación para usuario ID: {}", idUsuario);
        
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
        
        Notificacion notificacion = Notificacion.builder()
                .usuario(usuario)
                .mensaje(mensaje)
                .tipo(tipo)
                .leida(false)
                .build();
        
        Notificacion notificacionGuardada = notificacionRepository.save(notificacion);
        log.info("Notificación creada exitosamente con ID: {}", notificacionGuardada.getId());
        
        return mapearADTO(notificacionGuardada);
    }
    
    /**
     * Crea una notificación y opcionalmente envía email
     */
    @Transactional
    public NotificacionDTO crearNotificacion(NotificacionCreateDTO dto) {
        NotificacionDTO notificacion = crearNotificacion(
                dto.getIdUsuario(), 
                dto.getMensaje(), 
                dto.getTipo()
        );
        
        if (dto.getEnviarEmail()) {
            Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            enviarNotificacionPorEmail(usuario.getEmail(), dto.getMensaje(), dto.getTipo());
        }
        
        return notificacion;
    }
    
    /**
     * Envía notificación por email
     */
    public void enviarNotificacionPorEmail(String destinatario, String mensaje, 
                                          TipoNotificacion tipo) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(destinatario);
            mailMessage.setSubject("Gestor SAT - " + tipo.getDescripcion());
            mailMessage.setText(mensaje);
            mailMessage.setFrom("noreply@gestorsat.com");
            
            mailSender.send(mailMessage);
            log.info("Email enviado exitosamente a: {}", destinatario);
            
        } catch (Exception e) {
            log.error("Error al enviar email a: {}", destinatario, e);
        }
    }
    
    /**
     * Lista notificaciones de un usuario
     */
    public Page<NotificacionDTO> listarNotificacionesUsuario(Long idUsuario, 
                                                             Pageable pageable) {
        log.info("Listando notificaciones del usuario ID: {}", idUsuario);
        
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
        
        return notificacionRepository.findByUsuarioOrderByCreatedAtDesc(usuario, pageable)
                .map(this::mapearADTO);
    }
    
    /**
     * Obtiene notificaciones no leídas
     */
    public List<NotificacionDTO> obtenerNotificacionesNoLeidas(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
        
        return notificacionRepository.findByUsuarioAndLeidaFalseOrderByCreatedAtDesc(usuario)
                .stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Marca una notificación como leída
     */
    @Transactional
    public NotificacionDTO marcarComoLeida(Long idNotificacion) {
        log.info("Marcando notificación como leída ID: {}", idNotificacion);
        
        Notificacion notificacion = notificacionRepository.findById(idNotificacion)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + idNotificacion));
        
        notificacion.setLeida(true);
        notificacion.setFechaLectura(LocalDateTime.now());
        
        Notificacion notificacionActualizada = notificacionRepository.save(notificacion);
        return mapearADTO(notificacionActualizada);
    }
    
    /**
     * Cuenta notificaciones no leídas
     */
    public Long contarNotificacionesNoLeidas(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
        
        return notificacionRepository.countByUsuarioAndLeidaFalse(usuario);
    }
    
    private NotificacionDTO mapearADTO(Notificacion notificacion) {
        return NotificacionDTO.builder()
                .id(notificacion.getId())
                .mensaje(notificacion.getMensaje())
                .tipo(notificacion.getTipo())
                .leida(notificacion.getLeida())
                .fechaCreacion(notificacion.getCreatedAt())
                .fechaLectura(notificacion.getFechaLectura())
                .build();
    }
}