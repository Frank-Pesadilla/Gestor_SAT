package com.gestor.gestor_sat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {
    private Long id;
    private String nombre;
    private String dpi;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String direccion;
    private Integer edad;
    private LocalDateTime fechaCreacion;
    private String nombreUsuario;
}