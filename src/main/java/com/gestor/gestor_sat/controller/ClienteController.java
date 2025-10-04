package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.dto.ClienteCreateDTO;
import com.gestor.gestor_sat.dto.ClienteResponseDTO;
import com.gestor.gestor_sat.service.ClienteService;
import com.gestor.gestor_sat.service.TramiteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {
    
    private final ClienteService clienteService;
    
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> registrarCliente(
            @Valid @RequestBody ClienteCreateDTO clienteCreateDTO) {
        
        ClienteResponseDTO clienteRegistrado = clienteService.registrarCliente(clienteCreateDTO);
        return new ResponseEntity<>(clienteRegistrado, HttpStatus.CREATED);
    }

    private final TramiteService tramiteService; // Agregar esta inyección 
 
// Agregar este endpoint: 
@GetMapping("/{id}/historial") 
public ResponseEntity<Page<HistorialTramiteDTO>> obtenerHistorialCliente( 
        @PathVariable Long id, 
        @RequestParam(defaultValue = "0") int page, 
        @RequestParam(defaultValue = "10") int size) { 
     
    Pageable pageable = PageRequest.of(page, size); 
    Page<HistorialTramiteDTO> historial = tramiteService.obtenerHistorialCliente(id, pageable); 
    return ResponseEntity.ok(historial); 
}
}