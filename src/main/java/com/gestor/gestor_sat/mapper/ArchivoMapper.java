package com.gestor.gestor_sat.mapper;

import com.gestor.gestor_sat.dto.ArchivoResponseDTO;
import com.gestor.gestor_sat.entity.Archivo;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre Archivo entity y DTOs
 */
@Component
public class ArchivoMapper {

    /**
     * Convierte entidad Archivo a ArchivoResponseDTO
     */
    public ArchivoResponseDTO toResponseDTO(Archivo archivo) {
        return ArchivoResponseDTO.builder()
                .idArchivos(archivo.getIdArchivos())
                .nombreArchivo(archivo.getNombreArchivo())
                .ruta(archivo.getRuta())
                .idTramite(archivo.getTramite() != null ? archivo.getTramite().getIdTramites() : null)
                .nombreTramite(archivo.getTramite() != null ? archivo.getTramite().getNombre() : null)
                .idCliente(archivo.getCliente() != null ? archivo.getCliente().getIdCliente() : null)
                .nombreCliente(archivo.getCliente() != null ? archivo.getCliente().getNombreCompleto() : null)
                .fechaSubida(archivo.getFechaSubida())
                .createdAt(archivo.getCreatedAt())
                .build();
    }
}