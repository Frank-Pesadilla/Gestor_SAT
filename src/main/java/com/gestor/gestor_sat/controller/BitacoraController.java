package com.gestor.gestor_sat.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestor.gestor_sat.dto.BitacoraDTO;
import com.gestor.gestor_sat.dto.BitacoraFiltroDTO;
import com.gestor.gestor_sat.service.BitacoraService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bitacora")
@RequiredArgsConstructor
public class BitacoraController {
    
    private final BitacoraService bitacoraService;
    
    /**
     * Consulta la bitácora con filtros opcionales
     * GET /api/bitacora
     */
    @GetMapping
    public ResponseEntity<Page<BitacoraDTO>> consultarBitacora(
            BitacoraFiltroDTO filtros,
            Pageable pageable) {
        
        Page<BitacoraDTO> bitacoras = bitacoraService.consultarBitacora(filtros, pageable);
        return ResponseEntity.ok(bitacoras);
    }
}
