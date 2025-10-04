package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.TramiteCreateDTO;
import com.gestor.gestor_sat.dto.TramiteResponseDTO;
import com.gestor.gestor_sat.exception.ClienteNoEncontradoException;
import com.gestor.gestor_sat.exception.TipoTramiteNoEncontradoException;
import com.gestor.gestor_sat.mapper.TramiteMapper;
import com.gestor.gestor_sat.model.TipoTramite;
import com.gestor.gestor_sat.model.Tramite;
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
import com.gestor.gestor_sat.dto.HistorialTramiteDTO; 
<<<<<<< HEAD
<<<<<<< HEAD
=======
import com.gestor.gestor_sat.model.Archivo;
import com.gestor.gestor_sat.model.Cliente;
>>>>>>> Mafer
=======
import com.gestor.gestor_sat.model.Archivo;
import com.gestor.gestor_sat.model.Cliente;
>>>>>>> Mafer
import com.gestor.gestor_sat.model.ConsultaTramite;

import java.util.stream.Collectors; 
@Service
@RequiredArgsConstructor
@Slf4j



public class TramiteService {
    
    private final TramiteRepository tramiteRepository;
    private final TipoTramiteRepository tipoTramiteRepository;
    private final TramiteMapper tramiteMapper;
    private final ClienteRepository clienteRepository;
    private final ConsultaTramiteRepository consultaTramiteRepository;
    
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

<<<<<<< HEAD
    @Transactional(readOnly = true)
    public Page<HistorialTramiteDTO> obtenerHistorialCliente(Long idCliente, Pageable pageable) {
        // Validar que el cliente existe
        clienteRepository.findById(idCliente)
                .orElseThrow(() -> new ClienteNoEncontradoException(
                    "Cliente con ID " + idCliente + " no encontrado"));
        
        Page<ConsultaTramite> consultas = consultaTramiteRepository
                .findByClienteIdClienteOrderByFechaTramiteDesc(idCliente, pageable);
        
        return consultas.map(this::convertirAHistorialDTO);
    }

    private HistorialTramiteDTO convertirAHistorialDTO(ConsultaTramite consulta) {
        return HistorialTramiteDTO.builder()
                .idConsulta(consulta.getIdConsultaTramite())
                .idTramite(consulta.getTramite().getId())
                .nombreTramite(consulta.getTramite().getNombre())
                .descripcionTramite(consulta.getTramite().getDescripcion())
                .tipoTramite(consulta.getTramite().getTipoTramite().getNombre())
                .estado(consulta.getEstado().name())
                .fechaCreacion(consulta.getFechaTramite())
                .fechaFinalizacion(null)
                .observaciones(null)
                .archivos(null)
                .build();
    }

=======
>>>>>>> Mafer
    @Transactional(readOnly = true) 
public Page<HistorialTramiteDTO> obtenerHistorialCliente(Long idCliente, Pageable pageable) { 
    Cliente cliente = clienteRepository.findById(idCliente) 
            .orElseThrow(() -> new ClienteNoEncontradoException( 
                "Cliente con ID " + idCliente + " no encontrado")); 
     
    Page<ConsultaTramite> consultas = consultaTramiteRepository 
            .findByClienteIdOrderByFechaCreacionDesc(idCliente, pageable); 
     
    return consultas.map(this::convertirAHistorialDTO); 
} 
 
private HistorialTramiteDTO convertirAHistorialDTO(ConsultaTramite consulta) { 
    return HistorialTramiteDTO.builder() 
            .idConsulta(consulta.getId()) 
            .idTramite(consulta.getTramite().getId()) 
            .nombreTramite(consulta.getTramite().getNombre()) 
            .descripcionTramite(consulta.getTramite().getDescripcion()) 
            .tipoTramite(consulta.getTramite().getTipoTramite().getNombre()) 
            .estado(consulta.getEstado().name()) 
            .fechaCreacion(consulta.getFechaCreacion()) 
            .fechaFinalizacion(consulta.getFechaFinalizacion()) 
            .observaciones(consulta.getObservaciones()) 
            .archivos(convertirArchivosDTO(consulta.getArchivos())) 
            .build(); 
} 
 
private List<HistorialTramiteDTO.ArchivoInfoDTO> convertirArchivosDTO(List<Archivo> archivos) { 
    if (archivos == null) return null; 
    return archivos.stream() 
            .map(archivo -> HistorialTramiteDTO.ArchivoInfoDTO.builder() 
                    .id(archivo.getId()) 
                    .nombreArchivo(archivo.getNombreArchivo()) 
                    .tipoMime(archivo.getTipoMime()) 
                    .tamano(archivo.getTamano()) 
                    .fechaSubida(archivo.getFechaSubida()) 
                    .build()) 
            .collect(Collectors.toList()); 
} 
    
}