package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.SATResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.http.ResponseEntity;

@Service
@RequiredArgsConstructor
@Slf4j
public class IntegracionService {

    private final RestTemplate restTemplate;

    // Valida formato básico de NIT de Guatemala: dígitos + posible guion y dígito verificador
    private void validarNit(String nit) {
        if (nit == null || !nit.matches("^[0-9]{6,12}-?[0-9Xx]$")) {
            throw new IllegalArgumentException("Formato de NIT inválido");
        }
    }

    public SATResponseDTO consultarSAT(String nit) {
        validarNit(nit); // 2.6
        try {
            // 2.4 llamada HTTP (URL ejem: reemplaza por la real)
            String url = "https://api.sat.gob.gt/contribuyentes?nit={nit}";
            ResponseEntity<SATResponseDTO> resp =
                    restTemplate.getForEntity(url, SATResponseDTO.class, nit);
            return resp.getBody();
        } catch (ResourceAccessException e) {
            log.error("Timeout/conexión al consultar SAT: {}", e.getMessage());
            // 2.5 manejo de errores
            throw new RuntimeException("No se pudo consultar SAT: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error consultando SAT: ", e);
            throw new RuntimeException("Error consultando SAT: " + e.getMessage());
        }
    }

    // 2.8 Mock para desarrollo sin conexión real
    public SATResponseDTO consultarSATMock(String nit) {
        validarNit(nit);
        return SATResponseDTO.builder()
                .nit(nit)
                .nombre("Contribuyente Demo S.A.")
                .estado("ACTIVO")
                .regimen("GENERAL")
                .domicilio("Zona 4, Ciudad de Guatemala")
                .build();
    }
}
