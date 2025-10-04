package com.gestor.gestor_sat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArchivoCreateDTO {
    
    @NotBlank(message = "El nombre del archivo es obligatorio")
    private String nombreArchivo;
    
    @NotBlank(message = "La URL de Google Drive es obligatoria")
    private String urlDrive;
    
    @NotNull(message = "El ID del trámite es obligatorio")
    private Long idTramite;
    
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long idCliente;
    
    private String descripcion;
}