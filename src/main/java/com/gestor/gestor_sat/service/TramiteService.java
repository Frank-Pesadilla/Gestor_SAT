package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.TramiteCreateDTO;
import com.gestor.gestor_sat.dto.TramiteResponseDTO;
import com.gestor.gestor_sat.exception.TipoTramiteNoEncontradoException;
import com.gestor.gestor_sat.mapper.TramiteMapper;
import com.gestor.gestor_sat.model.TipoTramite;
import com.gestor.gestor_sat.model.Tramite;
import com.gestor.gestor_sat.repository.TipoTramiteRepository;
import com.gestor.gestor_sat.repository.TramiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TramiteService {
    
    private final TramiteRepository tramiteRepository;
    private final TipoTramiteRepository tipoTramiteRepository;
    private final TramiteMapper tramiteMapper;
    
    @Transactional
    public TramiteResponseDTO registrarTramite(TramiteCreateDTO dto) {
        log.info("Iniciando registro de trámite: {}", dto.getNombre());
        
        // Validar que el tipo de trámite exista
        TipoTramite tipoTramite = tipoTramiteRepository.findById(dto.getIdTipoTramite())
                .orElseThrow(() -> {
                    log.error("Tipo de trámite no encontrado: {}", dto.getIdTipoTramite());
                    return new TipoTramiteNoEncontradoException(dto.getIdTipoTramite());
                });
        
        // Convertir DTO a entidad
        Tramite tramite = tramiteMapper.toEntity(dto);
        tramite.setTipoTramite(tipoTramite);
        
        // Guardar trámite
        Tramite tramiteGuardado = tramiteRepository.save(tramite);
        log.info("Trámite registrado exitosamente con ID: {}", tramiteGuardado.getId());
        
        return tramiteMapper.toDTO(tramiteGuardado);
    }
}