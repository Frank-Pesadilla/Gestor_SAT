package com.gestor.gestor_sat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para operaciones de recuperación de contraseña
 * CU-SAT013
 *
 * Usado para retornar mensajes de éxito o error
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecuperacionResponseDTO {

    /**
     * Mensaje descriptivo de la operación
     */
    private String mensaje;

    /**
     * Indica si la operación fue exitosa
     */
    private Boolean exito;
}
