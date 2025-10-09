package com.gestor.gestor_sat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO para crear tipo de trámite
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoTramiteCreateDTO {

    @NotBlank(message = "El portal es obligatorio")
    @Pattern(regexp = "^(SAT|RENAP|IGSS|MINTRAB|OTRO)$", 
             message = "El portal debe ser: SAT, RENAP, IGSS, MINTRAB u OTRO")
    private String portal;

    @Size(max = 500, message = "El link no puede exceder 500 caracteres")
    private String link;
}