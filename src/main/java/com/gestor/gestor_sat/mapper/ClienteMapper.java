package com.gestor.gestor_sat.mapper;

import com.gestor.gestor_sat.dto.ClienteCreateDTO;
import com.gestor.gestor_sat.dto.ClienteResponseDTO;
import com.gestor.gestor_sat.entity.Cliente;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre Cliente entity y DTOs
 */
@Component
public class ClienteMapper {

    /**
     * Convierte ClienteCreateDTO a entidad Cliente
     */
    public Cliente toEntity(ClienteCreateDTO dto) {
        return Cliente.builder()
                .nombreCompleto(dto.getNombreCompleto())
                .dpi(dto.getDpi())
                .fechaNacimiento(dto.getFechaNacimiento())
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .build();
    }

    /**
     * Convierte entidad Cliente a ClienteResponseDTO
     */
    public ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return ClienteResponseDTO.builder()
                .idCliente(cliente.getIdCliente())
                .nombreCompleto(cliente.getNombreCompleto())
                .dpi(cliente.getDpi())
                .fechaNacimiento(cliente.getFechaNacimiento())
                .telefono(cliente.getTelefono())
                .direccion(cliente.getDireccion())
                .idUsuario(cliente.getUsuario() != null ? cliente.getUsuario().getIdUsuario() : null)
                .emailUsuario(cliente.getUsuario() != null ? cliente.getUsuario().getEmail() : null)
                .createdAt(cliente.getCreatedAt())
                .updatedAt(cliente.getUpdatedAt())
                .build();
    }
}