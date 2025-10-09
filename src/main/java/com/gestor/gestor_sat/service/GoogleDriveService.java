package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.config.GoogleDriveConfig;
import com.gestor.gestor_sat.exception.CustomExceptions;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

/**
 * Servicio para interactuar con Google Drive API
 */
@Service
@Slf4j
public class GoogleDriveService {

    private final Drive driveClient;
    private final GoogleDriveConfig googleDriveConfig;

    public GoogleDriveService(@Qualifier("driveClient") Drive driveClient, 
                              GoogleDriveConfig googleDriveConfig) {
        this.driveClient = driveClient;
        this.googleDriveConfig = googleDriveConfig;
    }

    /**
     * Sube un archivo a Google Drive
     * @param multipartFile archivo a subir
     * @return URL pública o ID del archivo en Drive
     */
    public String subirArchivo(MultipartFile multipartFile) {
        try {
            log.info("Subiendo archivo a Google Drive: {}", multipartFile.getOriginalFilename());

            // Generar nombre único para el archivo
            String extension = obtenerExtension(multipartFile.getOriginalFilename());
            String nombreUnico = UUID.randomUUID().toString() + extension;

            // Crear metadata del archivo
            File fileMetadata = new File();
            fileMetadata.setName(nombreUnico);
            fileMetadata.setParents(Collections.singletonList(googleDriveConfig.getFolderId()));

            // Preparar el contenido del archivo
            InputStreamContent mediaContent = new InputStreamContent(
                    multipartFile.getContentType(),
                    new ByteArrayInputStream(multipartFile.getBytes())
            );

            // Subir el archivo
            File uploadedFile = driveClient.files().create(fileMetadata, mediaContent)
                    .setFields("id, name, webViewLink, webContentLink")
                    .execute();

            log.info("Archivo subido exitosamente a Drive. ID: {}", uploadedFile.getId());

            // Retornar la URL de visualización del archivo
            return uploadedFile.getWebViewLink() != null ? 
                   uploadedFile.getWebViewLink() : 
                   "https://drive.google.com/file/d/" + uploadedFile.getId();

        } catch (IOException e) {
            log.error("Error al subir archivo a Google Drive: {}", e.getMessage());
            throw new CustomExceptions.ErrorAlmacenamientoArchivoException(
                    "Error al subir archivo a Google Drive: " + e.getMessage());
        }
    }

    /**
     * Obtiene la URL de descarga directa de un archivo
     * @param fileId ID del archivo en Google Drive
     * @return URL de descarga
     */
    public String obtenerUrlDescarga(String fileId) {
        try {
            File file = driveClient.files().get(fileId)
                    .setFields("webContentLink, webViewLink")
                    .execute();

            return file.getWebContentLink() != null ? 
                   file.getWebContentLink() : 
                   file.getWebViewLink();

        } catch (IOException e) {
            log.error("Error al obtener URL de descarga: {}", e.getMessage());
            throw new CustomExceptions.ArchivoNoEncontradoException(
                    Long.parseLong(fileId.replaceAll("\\D+", "")));
        }
    }

    /**
     * Elimina un archivo de Google Drive
     * @param fileId ID del archivo a eliminar
     */
    public void eliminarArchivo(String fileId) {
        try {
            driveClient.files().delete(fileId).execute();
            log.info("Archivo eliminado de Google Drive. ID: {}", fileId);
        } catch (IOException e) {
            log.error("Error al eliminar archivo de Google Drive: {}", e.getMessage());
            throw new CustomExceptions.ErrorAlmacenamientoArchivoException(
                    "Error al eliminar archivo: " + e.getMessage());
        }
    }

    /**
     * Extrae el ID del archivo de una URL de Google Drive
     * @param url URL de Google Drive
     * @return ID del archivo
     */
    public String extraerFileId(String url) {
        // Formatos comunes de URL de Google Drive:
        // https://drive.google.com/file/d/FILE_ID/view
        // https://drive.google.com/open?id=FILE_ID
        
        if (url.contains("/d/")) {
            String[] parts = url.split("/d/");
            if (parts.length > 1) {
                return parts[1].split("/")[0];
            }
        } else if (url.contains("id=")) {
            String[] parts = url.split("id=");
            if (parts.length > 1) {
                return parts[1].split("&")[0];
            }
        }
        
        // Si no es URL, asumir que ya es un ID
        return url;
    }

    /**
     * Obtiene la extensión del archivo
     */
    private String obtenerExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}