package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.dto.TramiteCreateDTO;
import com.gestor.gestor_sat.dto.TramiteResponseDTO;
import com.gestor.gestor_sat.service.TramiteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tramites")
@RequiredArgsConstructor
public class TramiteController {
    
    private final TramiteService tramiteService;
    
    /**
     * Registra un nuevo trámite
     * POST /api/tramites
     */
    @PostMapping
    public ResponseEntity<TramiteResponseDTO> registrarTramite(
            @Valid @RequestBody TramiteCreateDTO tramiteCreateDTO) {
        
        TramiteResponseDTO tramiteRegistrado = tramiteService.registrarTramite(tramiteCreateDTO);
        return new ResponseEntity<>(tramiteRegistrado, HttpStatus.CREATED);
    }
}