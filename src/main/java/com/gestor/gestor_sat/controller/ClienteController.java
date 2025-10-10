package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.dto.ClienteCreateDTO;
import com.gestor.gestor_sat.dto.ClienteResponseDTO;
import com.gestor.gestor_sat.dto.HistorialTramiteDTO;
import com.gestor.gestor_sat.service.ClienteService;
import com.gestor.gestor_sat.service.TramiteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestión de clientes
 * CU-SAT001: Registrar Cliente
 * CU-SAT005: Historial de Trámites
 */
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;
    private final TramiteService tramiteService; // CU-SAT005

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

    /**
     * CU-SAT005: Historial de Trámites - Tarea 6
     * Endpoint GET /api/clientes/{id}/historial
     * Obtiene el historial completo de trámites de un cliente
     *
     * Tarea 7: Soporta paginación con parámetros:
     * - page: número de página (default 0)
     * - size: tamaño de página (default 10)
     * - sort: campo de ordenamiento (default fechaTramite,desc)
     *
     * @param id ID del cliente
     * @param page Número de página (opcional, default 0)
     * @param size Tamaño de página (opcional, default 10)
     * @param sort Campo y dirección de ordenamiento (opcional, default fechaTramite,desc)
     * @return Página con el historial de trámites del cliente
     */
    @GetMapping("/{id}/historial")
    public ResponseEntity<Page<HistorialTramiteDTO>> obtenerHistorialCliente(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fechaTramite,desc") String sort) {

        log.info("GET /api/clientes/{}/historial - page: {}, size: {}, sort: {}",
                id, page, size, sort);

        // Tarea 7: Crear configuración de paginación
        // Por default ordena por fechaTramite descendente (más recientes primero)
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        // Obtener historial con paginación
        Page<HistorialTramiteDTO> historial = tramiteService.obtenerHistorialCliente(id, pageable);

        // Si el cliente no tiene trámites, retornar página vacía (no error)
        if (historial.isEmpty()) {
            log.info("Cliente ID: {} no tiene historial de trámites", id);
        }

        return ResponseEntity.ok(historial);
    }
}