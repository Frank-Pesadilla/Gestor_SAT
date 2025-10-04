package com.gestor.gestor_sat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.gestor.gestor_sat.dto.SATResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class IntegracionService {
    
    private final RestTemplate restTemplate;
    
    @Value("${sat.api.url:https://api.sat.gob.gt/consulta}")
    private String satApiUrl;
    
    @Value("${sat.api.mock:true}")
    private boolean usarMock;
    
    /**
     * Consulta información de un NIT en el SAT
     */
    public SATResponseDTO consultarSAT(String nit) {
        log.info("Consultando NIT en SAT: {}", nit);
        
        // Validar formato de NIT
        if (!validarFormatoNit(nit)) {
            throw new RuntimeException("Formato de NIT inválido");
        }
        
        // Si está en modo desarrollo, usar mock
        if (usarMock) {
            return consultarSATMock(nit);
        }
        
        try {
            String url = satApiUrl + "?nit=" + nit;
            SATResponseDTO response = restTemplate.getForObject(url, SATResponseDTO.class);
            log.info("Respuesta recibida del SAT para NIT: {}", nit);
            return response;
            
        } catch (HttpClientErrorException e) {
            log.error("Error HTTP al consultar SAT: {}", e.getMessage());
            return SATResponseDTO.builder()
                    .nit(nit)
                    .esValido(false)
                    .mensaje("Error al consultar SAT: " + e.getMessage())
                    .build();
                    
        } catch (ResourceAccessException e) {
            log.error("Error de conexión con SAT: {}", e.getMessage());
            return SATResponseDTO.builder()
                    .nit(nit)
                    .esValido(false)
                    .mensaje("Error de conexión con el servicio SAT")
                    .build();
        }
    }
    
    /**
     * Mock para desarrollo sin conexión real
     */
    private SATResponseDTO consultarSATMock(String nit) {
        log.info("Usando mock de SAT para NIT: {}", nit);
        
        return SATResponseDTO.builder()
                .nit(nit)
                .nombre("EMPRESA EJEMPLO S.A.")
                .estado("ACTIVO")
                .regimen("Régimen General")
                .direccion("Zona 10, Ciudad de Guatemala")
                .esValido(true)
                .mensaje("Consulta exitosa (MOCK)")
                .build();
    }
    
    /**
     * Valida formato de NIT guatemalteco
     */
    private boolean validarFormatoNit(String nit) {
        if (nit == null || nit.trim().isEmpty()) {
            return false;
        }
        // NIT: 8 dígitos + guion + 1 dígito verificador
        return nit.matches("\\d{7,8}-?\\d{1}");
    }
}