package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.dto.BusquedaAvanzadaDTO;
import com.gestor.gestor_sat.dto.ClienteResponseDTO;
import com.gestor.gestor_sat.service.BusquedaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/busqueda")
@RequiredArgsConstructor
@Slf4j
public class BusquedaController {

    private final BusquedaService busquedaService;

    @PostMapping("/avanzada")
    public ResponseEntity<Page<ClienteResponseDTO>> buscar(
            @RequestBody BusquedaAvanzadaDTO filtros,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idCliente,asc") String sort // "campo,direccion"
    ) {
        String[] parts = sort.split(",");
        Sort s = Sort.by(Sort.Direction.fromString(parts.length>1?parts[1]:"asc"), parts[0]);
        Pageable pageable = PageRequest.of(page, size, s);
        log.info("POST /api/busqueda/avanzada - filtros={}, pageable={}", filtros, pageable);
        return ResponseEntity.ok(busquedaService.buscarConFiltros(filtros, pageable));
    }
}
