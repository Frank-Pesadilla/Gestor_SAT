package com.gestor.gestor_sat.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatosPlataformaDTO {
    private Long id;
    
    @Pattern(regexp = "^[0-9]{1,8}-[0-9K]$", message = "Formato de NIT inválido")
    private String nit;
    
    private Integer nis;
    
    @Email
    private String email;
    
    @Pattern(regexp = "^[0-9]{13}$", message = "El DPI debe tener 13 dígitos")
    private String dpi;
    
    private String cuentaBancaria;
    
    // Contraseñas (encriptadas con AES)
    private String contrasenaAgenciaVirtual;
    private String contrasenaCorreo;
    private String contrasenaCgc;
    private String contrasenaConsultaGeneral;
    private String contrasenaRegahe;
}