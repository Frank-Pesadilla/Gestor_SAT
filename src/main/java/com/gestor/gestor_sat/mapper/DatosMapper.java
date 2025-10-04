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
                .id(entity.getId()) 
                .usuarioSat(entity.getUsuarioSat()) 
                .usuarioIgss(entity.getUsuarioIgss()) 
                .usuarioRenap(entity.getUsuarioRenap()) 
                .usuarioMinfin(entity.getUsuarioMinfin()) 
                .observaciones(entity.getObservaciones()) 
                .build(); 
         
        if (desencriptar) { 
            if (entity.getContrasenaSat() != null) { 
                dto.setContrasenaSat(aesEncryptionService.decrypt(entity.getContrasenaSat())); 
            } 
            if (entity.getContrasenaIgss() != null) { 
                dto.setContrasenaIgss(aesEncryptionService.decrypt(entity.getContrasenaIgss())); 
            } 
            if (entity.getContrasenaRenap() != null) { 
                dto.setContrasenaRenap(aesEncryptionService.decrypt(entity.getContrasenaRenap())); 
            } 
            if (entity.getContrasenaMinfin() != null) { 
                dto.setContrasenaMinfin(aesEncryptionService.decrypt(entity.getContrasenaMinfin())); 
            } 
        } else { 
            dto.setContrasenaSat("***PROTEGIDA***"); 
            dto.setContrasenaIgss("***PROTEGIDA***"); 
            dto.setContrasenaRenap("***PROTEGIDA***"); 
            dto.setContrasenaMinfin("***PROTEGIDA***"); 
        } 
         
        return dto; 
    } 
} 
