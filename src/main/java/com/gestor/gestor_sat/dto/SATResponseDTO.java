package com.gestor.gestor_sat.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SATResponseDTO {
    private String nit;
    private String nombre;
    private String estado;     // ACTIVO/INACTIVO
    private String regimen;    // General/Pequeño contribuyente, etc.
    private String domicilio;  // Opcional
}
