package com.gestor.gestor_sat.mapper;

import com.gestor.gestor_sat.dto.TipoTramiteCreateDTO;
import com.gestor.gestor_sat.dto.TipoTramiteResponseDTO;
import com.gestor.gestor_sat.entity.TipoTramite;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre TipoTramite entity y DTOs
 */
@Component
public class TipoTramiteMapper {

    /**
     * Convierte TipoTramiteCreateDTO a entidad TipoTramite
     */
    public TipoTramite toEntity(TipoTramiteCreateDTO dto) {
        return TipoTramite.builder()
                .portal(dto.getPortal())
                .link(dto.getLink())
                .build();
    }

    /**
     * Convierte entidad TipoTramite a TipoTramiteResponseDTO
     */
    public TipoTramiteResponseDTO toResponseDTO(TipoTramite tipoTramite) {
        return TipoTramiteResponseDTO.builder()
                .idTipoTramite(tipoTramite.getIdTipoTramite())
                .portal(tipoTramite.getPortal())
                .link(tipoTramite.getLink())
                .createdAt(tipoTramite.getCreatedAt())
                .updatedAt(tipoTramite.getUpdatedAt())
                .build();
    }
}