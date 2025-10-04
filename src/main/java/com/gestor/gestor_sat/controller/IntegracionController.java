package com.gestor.gestor_sat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestor.gestor_sat.dto.SATResponseDTO;
import com.gestor.gestor_sat.service.IntegracionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/integracion")
@RequiredArgsConstructor
public class IntegracionController {
    
    private final IntegracionService integracionService;
    
    /**
     * Consulta información de un NIT en el SAT
     * GET /api/integracion/sat?nit={nit}
     */
    @GetMapping("/sat")
    public ResponseEntity<SATResponseDTO> consultarSAT(@RequestParam String nit) {
        SATResponseDTO response = integracionService.consultarSAT(nit);
        return ResponseEntity.ok(response);
    }
}

