package com.gestor.gestor_sat.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO para crear un nuevo trámite
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TramiteCreateDTO {

    @NotBlank(message = "El nombre del trámite es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @NotNull(message = "El tipo de trámite es obligatorio")
    private Long idTipoTramite;
}