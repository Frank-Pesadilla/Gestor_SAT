package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.dto.ConsultaTramiteCreateDTO;
import com.gestor.gestor_sat.dto.ConsultaTramiteHistorialDTO;
import com.gestor.gestor_sat.dto.ConsultaTramiteResponseDTO;
import com.gestor.gestor_sat.service.ConsultaTramiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consulta-tramite")
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:5500", "http://127.0.0.1:5500",
        "http://localhost:5173", "http://localhost:3000"
})
public class ConsultaTramiteController {

    private final ConsultaTramiteService service;

    @PostMapping
    public ResponseEntity<ConsultaTramiteResponseDTO> crear(@RequestBody ConsultaTramiteCreateDTO dto) {
        return ResponseEntity.ok(service.crear(dto));
    }

    @GetMapping("/cliente/{idCliente}/historial")
    public ResponseEntity<List<ConsultaTramiteHistorialDTO>> historial(@PathVariable Long idCliente) {
        return ResponseEntity.ok(service.historialPorCliente(idCliente));
    }

    @GetMapping("/cliente/{idCliente}/historial-paged")
    public ResponseEntity<Page<ConsultaTramiteHistorialDTO>> historialPaged(
            @PathVariable Long idCliente,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.historialPorClientePaged(idCliente, page, size));
    }
}
