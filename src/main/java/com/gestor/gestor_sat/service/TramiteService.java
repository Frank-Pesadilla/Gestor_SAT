package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.HistorialTramiteDTO;
import com.gestor.gestor_sat.dto.TramiteCreateDTO;
import com.gestor.gestor_sat.dto.TramiteResponseDTO;
import com.gestor.gestor_sat.entity.Archivo;
import com.gestor.gestor_sat.entity.ConsultaTramite;
import com.gestor.gestor_sat.entity.TipoTramite;
import com.gestor.gestor_sat.entity.Tramite;
import com.gestor.gestor_sat.exception.CustomExceptions;
import com.gestor.gestor_sat.mapper.HistorialTramiteMapper;
import com.gestor.gestor_sat.mapper.TramiteMapper;
import com.gestor.gestor_sat.repository.ArchivoRepository;
import com.gestor.gestor_sat.repository.ClienteRepository;
import com.gestor.gestor_sat.repository.ConsultaTramiteRepository;
import com.gestor.gestor_sat.repository.TipoTramiteRepository;
import com.gestor.gestor_sat.repository.TramiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestión de trámites
 * CU-SAT003: Registrar Trámite
 * CU-SAT005: Historial de Trámites
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TramiteService {

    private final TramiteRepository tramiteRepository;
    private final TipoTramiteRepository tipoTramiteRepository;
    private final TramiteMapper tramiteMapper;

    // CU-SAT005: Dependencias para historial de trámites
    private final ConsultaTramiteRepository consultaTramiteRepository;
    private final ArchivoRepository archivoRepository;
    private final ClienteRepository clienteRepository;
    private final HistorialTramiteMapper historialTramiteMapper;

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

    /**
     * CU-SAT005: Historial de Trámites - Tarea 2
     * Obtiene el historial completo de trámites de un cliente específico
     *
     * Tarea 7: Implementa paginación para no retornar todos los registros a la vez
     * Tarea 5: Los resultados están ordenados por fecha descendente (más recientes primero)
     * Tarea 4: Incluye información de archivos asociados (nombres, rutas, tipos, fechas)
     *
     * @param idCliente ID del cliente
     * @param pageable Configuración de paginación (page, size, sort)
     * @return Página con el historial de trámites del cliente
     * @throws CustomExceptions.ClienteNoEncontradoException si el cliente no existe
     */
    @Transactional(readOnly = true)
    public Page<HistorialTramiteDTO> obtenerHistorialCliente(Long idCliente, Pageable pageable) {
        log.info("Obteniendo historial de trámites para cliente ID: {}", idCliente);

        // Validar que el cliente existe
        if (!clienteRepository.existsById(idCliente)) {
            log.error("Cliente no encontrado con ID: {}", idCliente);
            throw new CustomExceptions.ClienteNoEncontradoException(idCliente);
        }

        // Tarea 3 y 7: Obtener consultas de trámite con paginación, ordenadas por fecha DESC
        Page<ConsultaTramite> consultasPage = consultaTramiteRepository
                .findByClienteIdClienteWithPagination(idCliente, pageable);

        log.debug("Se encontraron {} consultas de trámite para el cliente ID: {}",
                consultasPage.getTotalElements(), idCliente);

        // Convertir cada ConsultaTramite a HistorialTramiteDTO incluyendo archivos
        Page<HistorialTramiteDTO> historialPage = consultasPage.map(consulta -> {
            // Tarea 4: Obtener archivos asociados al trámite y cliente
            List<Archivo> archivos = archivoRepository.findByClienteIdClienteAndTramiteIdTramites(
                    idCliente,
                    consulta.getTramite().getIdTramites()
            );

            log.debug("Trámite ID: {} tiene {} archivos adjuntos",
                    consulta.getTramite().getIdTramites(), archivos.size());

            // Mapear a DTO con archivos
            return historialTramiteMapper.toHistorialDTO(consulta, archivos);
        });

        log.info("Historial obtenido exitosamente: {} elementos, página {}/{}",
                historialPage.getNumberOfElements(),
                historialPage.getNumber() + 1,
                historialPage.getTotalPages());

        return historialPage;
    }
}