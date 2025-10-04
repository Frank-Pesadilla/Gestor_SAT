package com.gestor.gestor_sat.mapper;

import com.gestor.gestor_sat.dto.TramiteCreateDTO;
import com.gestor.gestor_sat.dto.TramiteResponseDTO;
import com.gestor.gestor_sat.model.Tramite;
import org.springframework.stereotype.Component;

@Component
public class TramiteMapper {
    
    public Tramite toEntity(TramiteCreateDTO dto) {
        return Tramite.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();
    }
    
    public TramiteResponseDTO toDTO(Tramite tramite) {
        return TramiteResponseDTO.builder()
                .id(tramite.getId())
                .nombre(tramite.getNombre())
                .descripcion(tramite.getDescripcion())
                .idTipoTramite(tramite.getTipoTramite() != null ? 
                              tramite.getTipoTramite().getId() : null)
                .nombreTipoTramite(tramite.getTipoTramite() != null ? 
                                  tramite.getTipoTramite().getNombre() : null)
                .fechaCreacion(tramite.getCreatedAt())
                .build();
    }
}