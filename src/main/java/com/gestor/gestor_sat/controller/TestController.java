package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.model.Usuario;
import com.gestor.gestor_sat.model.Cliente;
import com.gestor.gestor_sat.model.Tramite;
import com.gestor.gestor_sat.repository.UsuarioRepository;
import com.gestor.gestor_sat.repository.ClienteRepository;
import com.gestor.gestor_sat.repository.TramiteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;  // AGREGAR ESTO

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final TramiteRepository tramiteRepository;
    
    public TestController(
        UsuarioRepository usuarioRepository,
        ClienteRepository clienteRepository,
        TramiteRepository tramiteRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.tramiteRepository = tramiteRepository;
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Gestor SAT API funcionando correctamente");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }
    
    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> listarClientes() {
        return ResponseEntity.ok(clienteRepository.findAll());
    }
    
    @GetMapping("/tramites")
    @Transactional(readOnly = true)  // AGREGAR ESTA LÍNEA
    public ResponseEntity<List<Tramite>> listarTramites() {
        return ResponseEntity.ok(tramiteRepository.findAll());
    }
    
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Long>> estadisticas() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total_usuarios", usuarioRepository.count());
        stats.put("total_clientes", clienteRepository.count());
        stats.put("total_tramites", tramiteRepository.count());
        return ResponseEntity.ok(stats);
    }
}