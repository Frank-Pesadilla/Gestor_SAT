package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.ClienteCreateDTO;
import com.gestor.gestor_sat.dto.ClienteResponseDTO;
import com.gestor.gestor_sat.entity.Cliente;
import com.gestor.gestor_sat.entity.Usuario;
import com.gestor.gestor_sat.exception.CustomExceptions;
import com.gestor.gestor_sat.mapper.ClienteMapper;
import com.gestor.gestor_sat.repository.ClienteRepository;
import com.gestor.gestor_sat.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

/**
 * Servicio para gestión de clientes
 * CU-SAT001: Registrar Cliente
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteMapper clienteMapper;

    /**
     * Registra un nuevo cliente en el sistema
     * Implementa todas las reglas de negocio del CU-SAT001
     */
    @Transactional
    public ClienteResponseDTO registrarCliente(ClienteCreateDTO dto) {
        log.info("Iniciando registro de cliente con DPI: {}", dto.getDpi());

        // Tarea 5: Validar que el DPI no exista previamente
        if (clienteRepository.existsByDpi(dto.getDpi())) {
            log.error("El DPI {} ya está registrado", dto.getDpi());
            throw new CustomExceptions.DpiYaExisteException(dto.getDpi());
        }

        // Tarea 6: Validar que el cliente sea mayor de 18 años
        validarEdadMinima(dto.getFechaNacimiento());

        // Convertir DTO a entidad
        Cliente cliente = clienteMapper.toEntity(dto);

        // Tarea 7: Si se proporciona id_usuario, validar que exista y no tenga otro cliente
        if (dto.getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                    .orElseThrow(() -> new CustomExceptions.UsuarioNoEncontradoException(dto.getIdUsuario()));

            if (clienteRepository.existsByUsuarioId(dto.getIdUsuario())) {
                log.error("El usuario {} ya tiene un cliente asociado", dto.getIdUsuario());
                throw new CustomExceptions.UsuarioYaTieneClienteException(dto.getIdUsuario());
            }

            cliente.setUsuario(usuario);
        }

        // Tarea 8: Guardar el cliente
        Cliente clienteGuardado = clienteRepository.save(cliente);
        log.info("Cliente registrado exitosamente con ID: {}", clienteGuardado.getIdCliente());

        return clienteMapper.toResponseDTO(clienteGuardado);
    }

    /**
     * Valida que el cliente tenga al menos 18 años
     */
    private void validarEdadMinima(LocalDate fechaNacimiento) {
        LocalDate fechaActual = LocalDate.now();
        int edad = Period.between(fechaNacimiento, fechaActual).getYears();

        if (edad < 18) {
            log.error("Edad insuficiente: {} años", edad);
            throw new CustomExceptions.EdadMinimaException();
        }

        log.debug("Validación de edad exitosa: {} años", edad);
    }
}