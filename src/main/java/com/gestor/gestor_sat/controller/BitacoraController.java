package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.entity.Bitacora;
import com.gestor.gestor_sat.repository.BitacoraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bitacora")
@RequiredArgsConstructor
@Slf4j
public class BitacoraController {

    private final BitacoraRepository bitacoraRepository;

    @GetMapping
    public ResponseEntity<List<Bitacora>> consultar(
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) String accion,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {
        log.info("GET /api/bitacora - filtros usuario={}, accion={}, inicio={}, fin={}",
                usuario, accion, inicio, fin);

        if (inicio != null && fin != null) return ResponseEntity.ok(bitacoraRepository.findByFechaBetween(inicio, fin));
        if (usuario != null && !usuario.isBlank()) return ResponseEntity.ok(bitacoraRepository.findByUsuarioContainingIgnoreCase(usuario));
        if (accion != null && !accion.isBlank()) return ResponseEntity.ok(bitacoraRepository.findByAccionContainingIgnoreCase(accion));
        return ResponseEntity.ok(bitacoraRepository.findAll());
    }
}
