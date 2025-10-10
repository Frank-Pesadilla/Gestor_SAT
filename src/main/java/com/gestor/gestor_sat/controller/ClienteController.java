package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.dto.ClienteCreateDTO;
import com.gestor.gestor_sat.dto.ClienteDetalleDTO;
import com.gestor.gestor_sat.dto.ClienteResponseDTO;
import com.gestor.gestor_sat.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
/**
 * Controlador REST para gestión de clientes
 * CU-SAT001: Registrar Cliente
 */
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

     // 1.6 GET /api/clientes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDetalleDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/clientes/{} - Consultando detalle de cliente", id);
        return ResponseEntity.ok(clienteService.obtenerClientePorId(id));
    }

    // 1.7 GET /api/clientes/buscar?dpi={dpi}
    @GetMapping("/buscar")
    public ResponseEntity<ClienteDetalleDTO> buscarPorDpi(@RequestParam String dpi) {
        log.info("GET /api/clientes/buscar?dpi={} - Buscando cliente por DPI", dpi);
        return ResponseEntity.ok(clienteService.buscarClientePorDpi(dpi));
    }

    // 1.8 GET /api/clientes (paginado)
    @GetMapping
    public ResponseEntity<Page<ClienteResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("GET /api/clientes - Listando clientes page={}, size={}", page, size);
        return ResponseEntity.ok(clienteService.listarTodosLosClientes(pageable));
    }
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