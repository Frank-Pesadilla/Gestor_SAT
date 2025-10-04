package com.gestor.gestor_sat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TramiteCreateDTO {
    
    @NotBlank(message = "El nombre del trámite es obligatorio")
    @Size(max = 50, message = "El nombre no debe exceder 50 caracteres")
    private String nombre;
    
    @Size(max = 500, message = "La descripción no debe exceder 500 caracteres")
    private String descripcion;
    
    @NotNull(message = "El tipo de trámite es obligatorio")
    private Long idTipoTramite;
}