package com.gestor.gestor_sat.mapper;

import com.gestor.gestor_sat.dto.ClienteCreateDTO;
import com.gestor.gestor_sat.dto.ClienteResponseDTO;
import com.gestor.gestor_sat.model.Cliente;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.Period;

@Component
public class ClienteMapper {
    
    public Cliente toEntity(ClienteCreateDTO dto) {
        return Cliente.builder()
                .nombre(dto.getNombre())
                .dpi(dto.getDpi())
                .fechaNacimiento(dto.getFechaNacimiento())
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .build();
    }
    
    public ClienteResponseDTO toDTO(Cliente cliente) {
        Integer edad = calcularEdad(cliente.getFechaNacimiento());
        
        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .dpi(cliente.getDpi())
                .fechaNacimiento(cliente.getFechaNacimiento())
                .telefono(cliente.getTelefono())
                .direccion(cliente.getDireccion())
                .edad(edad)
                .fechaCreacion(cliente.getCreatedAt())
                .nombreUsuario(cliente.getUsuario() != null ? 
                              cliente.getUsuario().getNombre() : null)
                .build();
    }
    
    private Integer calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) return null;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}