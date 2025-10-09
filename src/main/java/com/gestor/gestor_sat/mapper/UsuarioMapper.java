package com.gestor.gestor_sat.mapper;

import com.gestor.gestor_sat.dto.UsuarioCreateDTO;
import com.gestor.gestor_sat.dto.UsuarioResponseDTO;
import com.gestor.gestor_sat.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre Usuario entity y DTOs
 */
@Component
@RequiredArgsConstructor
public class UsuarioMapper {

    private final PasswordEncoder passwordEncoder;

    /**
     * Convierte UsuarioCreateDTO a entidad Usuario
     * Hashea la contraseña con BCrypt
     */
    public Usuario toEntity(UsuarioCreateDTO dto) {
        return Usuario.builder()
                .usuario(dto.getUsuario())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword())) // BCrypt
                .build();
    }

    /**
     * Convierte entidad Usuario a UsuarioResponseDTO
     * NO incluye la contraseña
     */
    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .usuario(usuario.getUsuario())
                .email(usuario.getEmail())
                .estado(usuario.getEstado())
                .createdAt(usuario.getCreatedAt())
                .updatedAt(usuario.getUpdatedAt())
                .build();
    }
}