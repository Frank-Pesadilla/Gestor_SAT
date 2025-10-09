package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.dto.TramiteCreateDTO;
import com.gestor.gestor_sat.dto.TramiteResponseDTO;
import com.gestor.gestor_sat.service.TramiteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestión de trámites
 * CU-SAT003: Registrar Trámite
 */
@RestController
@RequestMapping("/api/tramites")
@RequiredArgsConstructor
@Slf4j
public class TramiteController {

    private final TramiteService tramiteService;

    /**
     * Tarea 7: Endpoint POST /api/tramites
     * Registra un nuevo trámite en el sistema
     */
    @PostMapping
    public ResponseEntity<TramiteResponseDTO> registrarTramite(@Valid @RequestBody TramiteCreateDTO dto) {
        log.info("POST /api/tramites - Registrando trámite: {}", dto.getNombre());
        
        TramiteResponseDTO response = tramiteService.registrarTramite(dto);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}