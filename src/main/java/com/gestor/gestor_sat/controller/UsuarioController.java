package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.dto.UsuarioCreateDTO;
import com.gestor.gestor_sat.dto.UsuarioResponseDTO;
import com.gestor.gestor_sat.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de usuarios
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Registra un nuevo usuario
     */
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@Valid @RequestBody UsuarioCreateDTO dto) {
        log.info("POST /api/usuarios - Registrando usuario: {}", dto.getUsuario());
        
        UsuarioResponseDTO response = usuarioService.registrarUsuario(dto);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Obtiene un usuario por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuario(@PathVariable Long id) {
        log.info("GET /api/usuarios/{} - Obteniendo usuario", id);
        
        UsuarioResponseDTO response = usuarioService.obtenerUsuarioPorId(id);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todos los usuarios
     */
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        log.info("GET /api/usuarios - Listando todos los usuarios");
        
        List<UsuarioResponseDTO> response = usuarioService.listarUsuarios();
        
        return ResponseEntity.ok(response);
    }

    /**
     * Busca usuario por email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorEmail(@PathVariable String email) {
        log.info("GET /api/usuarios/email/{} - Buscando usuario por email", email);
        
        UsuarioResponseDTO response = usuarioService.buscarPorEmail(email);
        
        return ResponseEntity.ok(response);
    }
}