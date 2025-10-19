package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.TipoTramiteCreateDTO;
import com.gestor.gestor_sat.dto.TipoTramiteResponseDTO;
import com.gestor.gestor_sat.entity.TipoTramite;
import com.gestor.gestor_sat.exception.CustomExceptions;
import com.gestor.gestor_sat.mapper.TipoTramiteMapper;
import com.gestor.gestor_sat.repository.TipoTramiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de tipos de trámite
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TipoTramiteService {

    private final TipoTramiteRepository tipoTramiteRepository;
    private final TipoTramiteMapper tipoTramiteMapper;

    /**
     * Registra un nuevo tipo de trámite
     */
    @Transactional
    public TipoTramiteResponseDTO registrarTipoTramite(TipoTramiteCreateDTO dto) {
        log.info("Registrando nuevo tipo de trámite: {}", dto.getPortal());

        TipoTramite tipoTramite = tipoTramiteMapper.toEntity(dto);
        TipoTramite tipoTramiteGuardado = tipoTramiteRepository.save(tipoTramite);

        log.info("Tipo de trámite registrado con ID: {}", tipoTramiteGuardado.getIdTipoTramite());
        return tipoTramiteMapper.toResponseDTO(tipoTramiteGuardado);
    }

    /**
     * Obtiene un tipo de trámite por ID
     */
    public TipoTramiteResponseDTO obtenerTipoTramitePorId(Long id) {
        log.info("Buscando tipo de trámite con ID: {}", id);
        
        TipoTramite tipoTramite = tipoTramiteRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.TipoTramiteNoEncontradoException(id));

        return tipoTramiteMapper.toResponseDTO(tipoTramite);
    }

    /**
     * Lista todos los tipos de trámite
     */
    public List<TipoTramiteResponseDTO> listarTiposTramite() {
        log.info("Listando todos los tipos de trámite");
        
        return tipoTramiteRepository.findAll().stream()
                .map(tipoTramiteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca tipo de trámite por portal
     */
    public TipoTramiteResponseDTO buscarPorPortal(String portal) {
        log.info("Buscando tipo de trámite por portal: {}", portal);
        
        TipoTramite tipoTramite = tipoTramiteRepository.findByPortal(portal)
                .orElseThrow(() -> new CustomExceptions.TipoTramiteNoEncontradoException("Portal: " + portal));

        return tipoTramiteMapper.toResponseDTO(tipoTramite);
    }
}