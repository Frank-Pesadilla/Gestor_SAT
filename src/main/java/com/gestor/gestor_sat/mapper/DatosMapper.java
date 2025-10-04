package com.gestor.gestor_sat.mapper;

import com.gestor.gestor_sat.dto.DatosPlataformaDTO;
import com.gestor.gestor_sat.model.Datos;
import com.gestor.gestor_sat.service.security.AESEncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatosMapper {
    
    private final AESEncryptionService aesEncryptionService;
    
    public DatosPlataformaDTO toDTO(Datos entity, boolean desencriptar) {
        if (entity == null) return null;
        
        DatosPlataformaDTO dto = DatosPlataformaDTO.builder()
                .id(entity.getIdDatos())
                .nit(entity.getNit())
                .nis(entity.getNis())
                .email(entity.getEmail())
                .dpi(entity.getDpi())
                .cuentaBancaria(entity.getCuentaBancaria())
                .build();
        
        if (desencriptar) {
            if (entity.getContrasenaAgenciaVirtual() != null) {
                dto.setContrasenaAgenciaVirtual(aesEncryptionService.decrypt(entity.getContrasenaAgenciaVirtual()));
            }
            if (entity.getContrasenaCorreo() != null) {
                dto.setContrasenaCorreo(aesEncryptionService.decrypt(entity.getContrasenaCorreo()));
            }
            if (entity.getContrasenaCgc() != null) {
                dto.setContrasenaCgc(aesEncryptionService.decrypt(entity.getContrasenaCgc()));
            }
            if (entity.getContrasenaConsultaGeneral() != null) {
                dto.setContrasenaConsultaGeneral(aesEncryptionService.decrypt(entity.getContrasenaConsultaGeneral()));
            }
            if (entity.getContrasenaRegahe() != null) {
                dto.setContrasenaRegahe(aesEncryptionService.decrypt(entity.getContrasenaRegahe()));
            }
        } else {
            dto.setContrasenaAgenciaVirtual("***PROTEGIDA***");
            dto.setContrasenaCorreo("***PROTEGIDA***");
            dto.setContrasenaCgc("***PROTEGIDA***");
            dto.setContrasenaConsultaGeneral("***PROTEGIDA***");
            dto.setContrasenaRegahe("***PROTEGIDA***");
        }
        
        return dto;
    }
}