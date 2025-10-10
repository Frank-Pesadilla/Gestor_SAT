package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.dto.SATResponseDTO;
import com.gestor.gestor_sat.service.IntegracionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/integracion")
@RequiredArgsConstructor
@Slf4j
public class IntegracionController {

    private final IntegracionService integracionService;

    // Real
    @GetMapping("/sat")
    public ResponseEntity<SATResponseDTO> consultarSAT(@RequestParam String nit) {
        log.info("GET /api/integracion/sat?nit={} - Consultando SAT", nit);
        return ResponseEntity.ok(integracionService.consultarSAT(nit));
    }

    // Mock
    @GetMapping("/sat/mock")
    public ResponseEntity<SATResponseDTO> consultarSATMock(@RequestParam String nit) {
        log.info("GET /api/integracion/sat/mock?nit={} - Consultando SAT (mock)", nit);
        return ResponseEntity.ok(integracionService.consultarSATMock(nit));
    }
}
