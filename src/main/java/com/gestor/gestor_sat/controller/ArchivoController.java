package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.dto.ArchivoResponseDTO;
import com.gestor.gestor_sat.service.ArchivoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para gestión de archivos
 * CU-SAT009: Gestión de Documentos
 * Los archivos se almacenan en Google Drive
 */
@RestController
@RequestMapping("/api/archivos")
@RequiredArgsConstructor
@Slf4j
public class ArchivoController {

    private final ArchivoService archivoService;

    /**
     * Tarea 11: Endpoint POST /api/archivos/upload
     * Sube un archivo a Google Drive
     * 
     * @param archivo El archivo a subir (PDF, JPG, PNG)
     * @param idTramite ID del trámite asociado
     * @param idCliente ID del cliente asociado
     * @return Información del archivo subido incluyendo la URL de Google Drive
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArchivoResponseDTO> subirArchivo(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam("idTramite") Long idTramite,
            @RequestParam("idCliente") Long idCliente) {
        
        log.info("POST /api/archivos/upload - Subiendo archivo: {}", archivo.getOriginalFilename());
        
        ArchivoResponseDTO response = archivoService.subirArchivo(archivo, idTramite, idCliente);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Tarea 12: Endpoint GET /api/archivos/{id}
     * Obtiene la información de un archivo incluyendo su URL en Google Drive
     * 
     * @param id ID del archivo
     * @return Información del archivo con URL de Google Drive
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArchivoResponseDTO> obtenerArchivo(@PathVariable Long id) {
        log.info("GET /api/archivos/{} - Obteniendo información del archivo", id);
        
        ArchivoResponseDTO response = archivoService.obtenerArchivo(id);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Tarea 13: Endpoint GET /api/archivos/{id}/url
     * Obtiene la URL de descarga directa del archivo en Google Drive
     * 
     * @param id ID del archivo
     * @return URL de Google Drive para descargar/visualizar el archivo
     */
    @GetMapping("/{id}/url")
    public ResponseEntity<Map<String, String>> obtenerUrlDescarga(@PathVariable Long id) {
        log.info("GET /api/archivos/{}/url - Obteniendo URL de descarga", id);
        
        String url = archivoService.obtenerUrlDescarga(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("url", url);
        response.put("mensaje", "Puedes acceder al archivo en Google Drive usando esta URL");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint DELETE /api/archivos/{id}
     * Elimina un archivo de Google Drive y de la base de datos
     * 
     * @param id ID del archivo
     * @return Mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarArchivo(@PathVariable Long id) {
        log.info("DELETE /api/archivos/{} - Eliminando archivo", id);
        
        archivoService.eliminarArchivo(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Archivo eliminado exitosamente de Google Drive");
        
        return ResponseEntity.ok(response);
    }
}