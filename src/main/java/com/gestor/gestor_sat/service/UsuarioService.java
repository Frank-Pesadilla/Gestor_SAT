package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.UsuarioCreateDTO;
import com.gestor.gestor_sat.dto.UsuarioResponseDTO;
import com.gestor.gestor_sat.entity.Usuario;
import com.gestor.gestor_sat.exception.CustomExceptions;
import com.gestor.gestor_sat.mapper.UsuarioMapper;
import com.gestor.gestor_sat.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de usuarios
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    /**
     * Registra un nuevo usuario en el sistema
     */
    @Transactional
    public UsuarioResponseDTO registrarUsuario(UsuarioCreateDTO dto) {
        log.info("Registrando nuevo usuario: {}", dto.getUsuario());

        // Validar que el usuario no exista
        if (usuarioRepository.existsByUsuario(dto.getUsuario())) {
            throw new CustomExceptions.UsuarioYaExisteException(dto.getUsuario());
        }

        // Validar que el email no exista
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new CustomExceptions.EmailYaExisteException(dto.getEmail());
        }

        // Convertir y guardar
        Usuario usuario = usuarioMapper.toEntity(dto);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        log.info("Usuario registrado exitosamente con ID: {}", usuarioGuardado.getIdUsuario());
        return usuarioMapper.toResponseDTO(usuarioGuardado);
    }

    /**
     * Obtiene un usuario por ID
     */
    public UsuarioResponseDTO obtenerUsuarioPorId(Long id) {
        log.info("Buscando usuario con ID: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.UsuarioNoEncontradoException(id));

        return usuarioMapper.toResponseDTO(usuario);
    }

    /**
     * Obtiene todos los usuarios
     */
    public List<UsuarioResponseDTO> listarUsuarios() {
        log.info("Listando todos los usuarios");
        
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un usuario por email
     */
    public UsuarioResponseDTO buscarPorEmail(String email) {
        log.info("Buscando usuario por email: {}", email);
        
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new CustomExceptions.UsuarioNoEncontradoException("Email: " + email));

        return usuarioMapper.toResponseDTO(usuario);
    }
}