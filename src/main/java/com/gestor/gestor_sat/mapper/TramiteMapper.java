package com.gestor.gestor_sat.mapper;

import com.gestor.gestor_sat.dto.TramiteCreateDTO;
import com.gestor.gestor_sat.dto.TramiteResponseDTO;
import com.gestor.gestor_sat.entity.Tramite;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre Tramite entity y DTOs
 */
@Component
public class TramiteMapper {

    /**
     * Convierte TramiteCreateDTO a entidad Tramite
     */
    public Tramite toEntity(TramiteCreateDTO dto) {
        return Tramite.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();
    }

    /**
     * Convierte entidad Tramite a TramiteResponseDTO
     */
    public TramiteResponseDTO toResponseDTO(Tramite tramite) {
        return TramiteResponseDTO.builder()
                .idTramites(tramite.getIdTramites())
                .nombre(tramite.getNombre())
                .descripcion(tramite.getDescripcion())
                .idTipoTramite(tramite.getTipoTramite() != null ? tramite.getTipoTramite().getIdTipoTramite() : null)
                .portalTipoTramite(tramite.getTipoTramite() != null ? tramite.getTipoTramite().getPortal() : null)
                .linkTipoTramite(tramite.getTipoTramite() != null ? tramite.getTipoTramite().getLink() : null)
                .createdAt(tramite.getCreatedAt())
                .updatedAt(tramite.getUpdatedAt())
                .build();
    }
}