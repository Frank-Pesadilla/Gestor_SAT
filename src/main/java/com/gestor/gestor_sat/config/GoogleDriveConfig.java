package com.gestor.gestor_sat.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/**
 * Configuración para Google Drive API
 */
@Configuration
@Getter
@Slf4j
public class GoogleDriveConfig {

    private static final String APPLICATION_NAME = "Gestor SAT";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);

    @Value("${google.drive.credentials-path}")
    private String credentialsPath;

    @Value("${google.drive.folder-id}")
    private String folderId;

    @Value("${app.upload.allowed-types}")
    private String[] allowedTypes;

    @Value("${app.upload.allowed-extensions}")
    private String[] allowedExtensions;

    /**
     * Crea el cliente de Google Drive
     * NOTA: El nombre del bean es "driveClient" para evitar conflicto con GoogleDriveService
     */
    @Bean(name = "driveClient")
    public Drive googleDriveClient() throws GeneralSecurityException, IOException {
        log.info("Inicializando servicio de Google Drive");

        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        
        // Cargar credenciales desde el archivo JSON
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(credentialsPath))
                .createScoped(SCOPES);

        Drive service = new Drive.Builder(
                httpTransport,
                JSON_FACTORY,
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();

        log.info("Servicio de Google Drive inicializado correctamente");
        return service;
    }
}