package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.TramiteCreateDTO;
import com.gestor.gestor_sat.dto.TramiteResponseDTO;
import com.gestor.gestor_sat.entity.TipoTramite;
import com.gestor.gestor_sat.entity.Tramite;
import com.gestor.gestor_sat.exception.CustomExceptions;
import com.gestor.gestor_sat.mapper.TramiteMapper;
import com.gestor.gestor_sat.repository.TipoTramiteRepository;
import com.gestor.gestor_sat.repository.TramiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para gestión de trámites
 * CU-SAT003: Registrar Trámite
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TramiteService {

    private final TramiteRepository tramiteRepository;
    private final TipoTramiteRepository tipoTramiteRepository;
    private final TramiteMapper tramiteMapper;

    /**
     * Registra un nuevo trámite en el sistema
     * Implementa todas las reglas de negocio del CU-SAT003
     */
    @Transactional
    public TramiteResponseDTO registrarTramite(TramiteCreateDTO dto) {
        log.info("Iniciando registro de trámite: {}", dto.getNombre());

        // Tarea 4: Validar que el id_tipo_tramite exista
        TipoTramite tipoTramite = tipoTramiteRepository.findById(dto.getIdTipoTramite())
                .orElseThrow(() -> new CustomExceptions.TipoTramiteNoEncontradoException(dto.getIdTipoTramite()));

        log.debug("Tipo de trámite encontrado: {}", tipoTramite.getPortal());

        // Tarea 5: Validar nombre (ya validado por @NotBlank y @Size en el DTO)
        // La validación de longitud y no vacío se maneja automáticamente

        // Convertir DTO a entidad
        Tramite tramite = tramiteMapper.toEntity(dto);
        tramite.setTipoTramite(tipoTramite);

        // Tarea 6: Guardar el trámite
        Tramite tramiteGuardado = tramiteRepository.save(tramite);
        log.info("Trámite registrado exitosamente con ID: {}", tramiteGuardado.getIdTramites());

        return tramiteMapper.toResponseDTO(tramiteGuardado);
    }
}