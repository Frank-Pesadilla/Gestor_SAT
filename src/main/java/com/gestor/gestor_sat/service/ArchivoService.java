package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.ArchivoResponseDTO;
import com.gestor.gestor_sat.entity.Archivo;
import com.gestor.gestor_sat.entity.Cliente;
import com.gestor.gestor_sat.entity.Tramite;
import com.gestor.gestor_sat.exception.CustomExceptions;
import com.gestor.gestor_sat.mapper.ArchivoMapper;
import com.gestor.gestor_sat.repository.ArchivoRepository;
import com.gestor.gestor_sat.repository.ClienteRepository;
import com.gestor.gestor_sat.repository.TramiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

/**
 * Servicio para gestión de archivos
 * CU-SAT009: Gestión de Documentos
 * Los archivos se guardan en Google Drive
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ArchivoService {

    private final ArchivoRepository archivoRepository;
    private final ClienteRepository clienteRepository;
    private final TramiteRepository tramiteRepository;
    private final ArchivoMapper archivoMapper;
    private final GoogleDriveService googleDriveService;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB en bytes

    /**
     * Sube un archivo a Google Drive y registra en BD
     * Implementa todas las reglas de negocio del CU-SAT009
     */
    @Transactional
    public ArchivoResponseDTO subirArchivo(MultipartFile file, Long idTramite, Long idCliente) {
        log.info("Iniciando subida de archivo para cliente {} y trámite {}", idCliente, idTramite);

        // Tarea 5: Validar que el archivo no exceda 10MB
        if (file.isEmpty()) {
            throw new CustomExceptions.ArchivoVacioException();
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            log.error("Archivo excede tamaño máximo: {} bytes", file.getSize());
            throw new CustomExceptions.TamanoArchivoExcedidoException();
        }

        // Tarea 6: Validar que el tipo de archivo sea permitido (PDF, JPG, PNG)
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        validarTipoArchivo(contentType, originalFilename);

        // Tarea 7: Validar que el trámite y cliente existan
        Tramite tramite = tramiteRepository.findById(idTramite)
                .orElseThrow(() -> new CustomExceptions.TramiteNoEncontradoException(idTramite));

        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new CustomExceptions.ClienteNoEncontradoException(idCliente));

        // Tarea 8 y 9: Subir archivo a Google Drive
        String urlGoogleDrive = googleDriveService.subirArchivo(file);
        log.info("Archivo subido a Google Drive: {}", urlGoogleDrive);

        // Tarea 10: Guardar registro en BD con la URL de Google Drive
        Archivo archivo = Archivo.builder()
                .nombreArchivo(originalFilename)
                .ruta(urlGoogleDrive) // URL del archivo en Google Drive
                .tramite(tramite)
                .cliente(cliente)
                .build();

        Archivo archivoGuardado = archivoRepository.save(archivo);
        log.info("Archivo registrado en BD con ID: {}", archivoGuardado.getIdArchivos());

        return archivoMapper.toResponseDTO(archivoGuardado);
    }

    /**
     * Obtiene la información de un archivo
     * Tarea 13: Retorna la información del archivo incluyendo la URL de Google Drive
     */
    public ArchivoResponseDTO obtenerArchivo(Long id) {
        log.info("Obteniendo información del archivo con ID: {}", id);

        Archivo archivo = archivoRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.ArchivoNoEncontradoException(id));

        return archivoMapper.toResponseDTO(archivo);
    }

    /**
     * Obtiene la URL de descarga de Google Drive
     */
    public String obtenerUrlDescarga(Long id) {
        log.info("Obteniendo URL de descarga para archivo ID: {}", id);

        Archivo archivo = archivoRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.ArchivoNoEncontradoException(id));

        // La ruta ya contiene la URL de Google Drive
        return archivo.getRuta();
    }

    /**
     * Elimina un archivo de Google Drive y de la BD
     */
    @Transactional
    public void eliminarArchivo(Long id) {
        log.info("Eliminando archivo con ID: {}", id);

        Archivo archivo = archivoRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.ArchivoNoEncontradoException(id));

        try {
            // Extraer el ID del archivo de la URL de Google Drive
            String fileId = googleDriveService.extraerFileId(archivo.getRuta());
            
            // Eliminar de Google Drive
            googleDriveService.eliminarArchivo(fileId);
            
            // Eliminar de la base de datos
            archivoRepository.delete(archivo);
            
            log.info("Archivo eliminado exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar archivo: {}", e.getMessage());
            throw new CustomExceptions.ErrorAlmacenamientoArchivoException(
                    "Error al eliminar archivo: " + e.getMessage());
        }
    }

    /**
     * Valida el tipo de archivo
     */
    private void validarTipoArchivo(String contentType, String filename) {
        String[] tiposPermitidos = {"application/pdf", "image/jpeg", "image/png"};
        String[] extensionesPermitidas = {".pdf", ".jpg", ".jpeg", ".png"};

        boolean tipoValido = Arrays.asList(tiposPermitidos).contains(contentType);
        boolean extensionValida = Arrays.stream(extensionesPermitidas)
                .anyMatch(ext -> filename != null && filename.toLowerCase().endsWith(ext));

        if (!tipoValido || !extensionValida) {
            log.error("Tipo de archivo no permitido: {} - {}", contentType, filename);
            throw new CustomExceptions.TipoArchivoNoPermitidoException(contentType);
        }

        log.debug("Tipo de archivo válido: {}", contentType);
    }
}