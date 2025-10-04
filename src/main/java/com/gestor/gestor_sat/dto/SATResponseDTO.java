package com.gestor.gestor_sat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SATResponseDTO {
    private String nit;
    private String nombre;
    private String estado; // ACTIVO, INACTIVO
    private String regimen; // Pequeño Contribuyente, General, etc.
    private String direccion;
    private Boolean esValido;
    private String mensaje;
}
