package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.dto.TipoTramiteCreateDTO;
import com.gestor.gestor_sat.dto.TipoTramiteResponseDTO;
import com.gestor.gestor_sat.service.TipoTramiteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de tipos de trámite
 */
@RestController
@RequestMapping("/api/tipos-tramite")
@RequiredArgsConstructor
@Slf4j
public class TipoTramiteController {

    private final TipoTramiteService tipoTramiteService;

    /**
     * Registra un nuevo tipo de trámite
     */
    @PostMapping
    public ResponseEntity<TipoTramiteResponseDTO> registrarTipoTramite(@Valid @RequestBody TipoTramiteCreateDTO dto) {
        log.info("POST /api/tipos-tramite - Registrando tipo de trámite: {}", dto.getPortal());
        
        TipoTramiteResponseDTO response = tipoTramiteService.registrarTipoTramite(dto);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Obtiene un tipo de trámite por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoTramiteResponseDTO> obtenerTipoTramite(@PathVariable Long id) {
        log.info("GET /api/tipos-tramite/{} - Obteniendo tipo de trámite", id);
        
        TipoTramiteResponseDTO response = tipoTramiteService.obtenerTipoTramitePorId(id);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todos los tipos de trámite
     */
    @GetMapping
    public ResponseEntity<List<TipoTramiteResponseDTO>> listarTiposTramite() {
        log.info("GET /api/tipos-tramite - Listando todos los tipos de trámite");
        
        List<TipoTramiteResponseDTO> response = tipoTramiteService.listarTiposTramite();
        
        return ResponseEntity.ok(response);
    }

    /**
     * Busca tipo de trámite por portal
     */
    @GetMapping("/portal/{portal}")
    public ResponseEntity<TipoTramiteResponseDTO> buscarPorPortal(@PathVariable String portal) {
        log.info("GET /api/tipos-tramite/portal/{} - Buscando por portal", portal);
        
        TipoTramiteResponseDTO response = tipoTramiteService.buscarPorPortal(portal);
        
        return ResponseEntity.ok(response);
    }
}