package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.dto.ClienteCreateDTO;
import com.gestor.gestor_sat.dto.ClienteResponseDTO;
import com.gestor.gestor_sat.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestión de clientes
 * CU-SAT001: Registrar Cliente
 */
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * Tarea 9: Endpoint POST /api/clientes
     * Registra un nuevo cliente en el sistema
     */
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> registrarCliente(@Valid @RequestBody ClienteCreateDTO dto) {
        log.info("POST /api/clientes - Registrando cliente con DPI: {}", dto.getDpi());
        
        ClienteResponseDTO response = clienteService.registrarCliente(dto);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}