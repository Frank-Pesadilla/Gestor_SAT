package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.dto.NotificacionCreateDTO;
import com.gestor.gestor_sat.dto.NotificacionResponseDTO;
import com.gestor.gestor_sat.service.NotificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestión de notificaciones
 * CU-SAT004: Generar Notificaciones
 */
@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
@Slf4j
public class NotificacionController {

    private final NotificacionService notificacionService;

    /**
     * Tarea 8: Endpoint GET /api/notificaciones/usuario/{id}
     * Lista todas las notificaciones de un usuario
     */
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<NotificacionResponseDTO>> listarNotificacionesUsuario(
            @PathVariable Long id) {
        log.info("GET /api/notificaciones/usuario/{} - Listando notificaciones", id);

        List<NotificacionResponseDTO> notificaciones = notificacionService.listarNotificacionesUsuario(id);

        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Lista notificaciones no leídas de un usuario
     */
    @GetMapping("/usuario/{id}/no-leidas")
    public ResponseEntity<List<NotificacionResponseDTO>> listarNotificacionesNoLeidas(
            @PathVariable Long id) {
        log.info("GET /api/notificaciones/usuario/{}/no-leidas - Listando notificaciones no leídas", id);

        List<NotificacionResponseDTO> notificaciones = notificacionService.listarNotificacionesNoLeidas(id);

        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Tarea 9: Endpoint PUT /api/notificaciones/{id}/marcar-leida
     * Marca una notificación como leída
     */
    @PutMapping("/{id}/marcar-leida")
    public ResponseEntity<NotificacionResponseDTO> marcarComoLeida(@PathVariable Long id) {
        log.info("PUT /api/notificaciones/{}/marcar-leida - Marcando notificación como leída", id);

        NotificacionResponseDTO notificacion = notificacionService.marcarComoLeida(id);

        return ResponseEntity.ok(notificacion);
    }

    /**
     * Crea una nueva notificación
     */
    @PostMapping
    public ResponseEntity<NotificacionResponseDTO> crearNotificacion(
            @Valid @RequestBody NotificacionCreateDTO dto) {
        log.info("POST /api/notificaciones - Creando notificación para usuario ID: {}", dto.getIdUsuario());

        NotificacionResponseDTO notificacion = notificacionService.crearNotificacionConEmail(
                dto.getIdUsuario(),
                dto.getMensaje(),
                dto.getTipo(),
                dto.getEnviarEmail()
        );

        return new ResponseEntity<>(notificacion, HttpStatus.CREATED);
    }

    /**
     * Cuenta notificaciones no leídas de un usuario
     */
    @GetMapping("/usuario/{id}/count-no-leidas")
    public ResponseEntity<Map<String, Long>> contarNotificacionesNoLeidas(@PathVariable Long id) {
        log.info("GET /api/notificaciones/usuario/{}/count-no-leidas", id);

        Long count = notificacionService.contarNotificacionesNoLeidas(id);

        Map<String, Long> response = new HashMap<>();
        response.put("count", count);

        return ResponseEntity.ok(response);
    }
}
