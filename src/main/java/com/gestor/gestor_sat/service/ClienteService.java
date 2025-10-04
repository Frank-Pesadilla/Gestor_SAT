package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.ClienteCreateDTO;
import com.gestor.gestor_sat.dto.ClienteResponseDTO;
import com.gestor.gestor_sat.exception.*;
import com.gestor.gestor_sat.mapper.ClienteMapper;
import com.gestor.gestor_sat.model.Cliente;
import com.gestor.gestor_sat.model.Usuario;
import com.gestor.gestor_sat.repository.ClienteRepository;
import com.gestor.gestor_sat.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteMapper clienteMapper;
    
    @Transactional
    public ClienteResponseDTO registrarCliente(ClienteCreateDTO dto) {
        log.info("Iniciando registro de cliente con DPI: {}", dto.getDpi());
        
        // Validación 1: Verificar que el DPI no exista
        if (clienteRepository.existsByDpi(dto.getDpi())) {
            log.error("DPI ya existe: {}", dto.getDpi());
            throw new DpiYaExisteException(dto.getDpi());
        }
        
        // Validación 2: Verificar que sea mayor de 18 años
        Integer edad = calcularEdad(dto.getFechaNacimiento());
        if (edad < 18) {
            log.error("Cliente menor de edad. Edad: {} años", edad);
            throw new EdadInsuficienteException(edad);
        }
        
        // Convertir DTO a entidad
        Cliente cliente = clienteMapper.toEntity(dto);
        
        // Validación 3: Si se proporciona ID de usuario, validar
        if (dto.getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getIdUsuario()));
            
            // Verificar que el usuario no tenga otro cliente asociado
            if (clienteRepository.existsByUsuario(usuario)) {
                log.error("Usuario {} ya tiene un cliente asociado", dto.getIdUsuario());
                throw new UsuarioYaTieneClienteException(dto.getIdUsuario());
            }
            
            cliente.setUsuario(usuario);
        }
        
        // Guardar cliente
        Cliente clienteGuardado = clienteRepository.save(cliente);
        log.info("Cliente registrado exitosamente con ID: {}", clienteGuardado.getId());
        
        return clienteMapper.toDTO(clienteGuardado);
    }
    
    private Integer calcularEdad(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}