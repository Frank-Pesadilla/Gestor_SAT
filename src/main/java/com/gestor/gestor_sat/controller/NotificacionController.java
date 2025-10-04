// NotificacionController.java
package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.dto.NotificacionCreateDTO;
import com.gestor.gestor_sat.dto.NotificacionDTO;
import com.gestor.gestor_sat.service.NotificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {
    
    private final NotificacionService notificacionService;
    
    /**
     * Crea una nueva notificación
     * POST /api/notificaciones
     */
    @PostMapping
    public ResponseEntity<NotificacionDTO> crearNotificacion(
            @Valid @RequestBody NotificacionCreateDTO dto) {
        
        NotificacionDTO notificacion = notificacionService.crearNotificacion(dto);
        return new ResponseEntity<>(notificacion, HttpStatus.CREATED);
    }
    
    /**
     * Lista notificaciones de un usuario
     * GET /api/notificaciones/usuario/{id}
     */
    @GetMapping("/usuario/{id}")
    public ResponseEntity<Page<NotificacionDTO>> listarNotificaciones(
            @PathVariable Long id,
            Pageable pageable) {
        
        Page<NotificacionDTO> notificaciones = 
            notificacionService.listarNotificacionesUsuario(id, pageable);
        return ResponseEntity.ok(notificaciones);
    }
    
    /**
     * Obtiene notificaciones no leídas
     * GET /api/notificaciones/usuario/{id}/no-leidas
     */
    @GetMapping("/usuario/{id}/no-leidas")
    public ResponseEntity<List<NotificacionDTO>> obtenerNoLeidas(@PathVariable Long id) {
        List<NotificacionDTO> notificaciones = 
            notificacionService.obtenerNotificacionesNoLeidas(id);
        return ResponseEntity.ok(notificaciones);
    }
    
    /**
     * Marca notificación como leída
     * PUT /api/notificaciones/{id}/marcar-leida
     */
    @PutMapping("/{id}/marcar-leida")
    public ResponseEntity<NotificacionDTO> marcarComoLeida(@PathVariable Long id) {
        NotificacionDTO notificacion = notificacionService.marcarComoLeida(id);
        return ResponseEntity.ok(notificacion);
    }
    
    /**
     * Cuenta notificaciones no leídas
     * GET /api/notificaciones/usuario/{id}/contador
     */
    @GetMapping("/usuario/{id}/contador")
    public ResponseEntity<Long> contarNoLeidas(@PathVariable Long id) {
        Long contador = notificacionService.contarNotificacionesNoLeidas(id);
        return ResponseEntity.ok(contador);
    }
}
