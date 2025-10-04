package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.dto.ArchivoCreateDTO;
import com.gestor.gestor_sat.dto.ArchivoResponseDTO;
import com.gestor.gestor_sat.service.ArchivoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/archivos")
@RequiredArgsConstructor
public class ArchivoController {
    
    private final ArchivoService archivoService;
    
    /**
     * Registra un archivo de Google Drive
     * POST /api/archivos
     */
    @PostMapping
    public ResponseEntity<ArchivoResponseDTO> registrarArchivo(
            @Valid @RequestBody ArchivoCreateDTO dto) {
        
        ArchivoResponseDTO archivo = archivoService.registrarArchivo(dto);
        return new ResponseEntity<>(archivo, HttpStatus.CREATED);
    }
    
    /**
     * Obtiene un archivo por ID
     * GET /api/archivos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArchivoResponseDTO> obtenerArchivo(@PathVariable Long id) {
        ArchivoResponseDTO archivo = archivoService.obtenerArchivo(id);
        return ResponseEntity.ok(archivo);
    }
}