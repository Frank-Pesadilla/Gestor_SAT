package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.ArchivoCreateDTO;
import com.gestor.gestor_sat.dto.ArchivoResponseDTO;
import com.gestor.gestor_sat.exception.ClienteNoEncontradoException;
import com.gestor.gestor_sat.model.Archivo;
import com.gestor.gestor_sat.model.Cliente;
import com.gestor.gestor_sat.model.Tramite;
import com.gestor.gestor_sat.repository.ArchivoRepository;
import com.gestor.gestor_sat.repository.ClienteRepository;
import com.gestor.gestor_sat.repository.TramiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArchivoService {
    
    private final ArchivoRepository archivoRepository;
    private final TramiteRepository tramiteRepository;
    private final ClienteRepository clienteRepository;
    
    /**
     * Registra un archivo de Google Drive
     */
    @Transactional
    public ArchivoResponseDTO registrarArchivo(ArchivoCreateDTO dto) {
        log.info("Registrando archivo: {} para cliente ID: {}", 
                 dto.getNombreArchivo(), dto.getIdCliente());
        
        // Validar que el trámite existe
        Tramite tramite = tramiteRepository.findById(dto.getIdTramite())
                .orElseThrow(() -> new RuntimeException("Trámite no encontrado con ID: " + dto.getIdTramite()));
        
        // Validar que el cliente existe
        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new ClienteNoEncontradoException(dto.getIdCliente()));
        
        // Crear registro del archivo
        Archivo archivo = Archivo.builder()
                .nombreArchivo(dto.getNombreArchivo())
                .urlDrive(dto.getUrlDrive())
                .descripcion(dto.getDescripcion())
                .tramite(tramite)
                .cliente(cliente)
                .build();
        
        Archivo archivoGuardado = archivoRepository.save(archivo);
        log.info("Archivo registrado exitosamente con ID: {}", archivoGuardado.getId());
        
        return ArchivoResponseDTO.builder()
                .id(archivoGuardado.getId())
                .nombreArchivo(archivoGuardado.getNombreArchivo())
                .urlDrive(archivoGuardado.getUrlDrive())
                .descripcion(archivoGuardado.getDescripcion())
                .idTramite(tramite.getId())
                .nombreTramite(tramite.getNombre())
                .idCliente(cliente.getId())
                .nombreCliente(cliente.getNombre())
                .fechaRegistro(archivoGuardado.getCreatedAt())
                .build();
    }
    
    /**
     * Obtiene un archivo por ID
     */
    public ArchivoResponseDTO obtenerArchivo(Long id) {
        log.info("Obteniendo archivo ID: {}", id);
        
        Archivo archivo = archivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado con ID: " + id));
        
        return ArchivoResponseDTO.builder()
                .id(archivo.getId())
                .nombreArchivo(archivo.getNombreArchivo())
                .urlDrive(archivo.getUrlDrive())
                .descripcion(archivo.getDescripcion())
                .idTramite(archivo.getTramite().getId())
                .nombreTramite(archivo.getTramite().getNombre())
                .idCliente(archivo.getCliente().getId())
                .nombreCliente(archivo.getCliente().getNombre())
                .fechaRegistro(archivo.getCreatedAt())
                .build();
    }
}