package com.gestor.gestor_sat.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para respuesta con información del cliente
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponseDTO {

    private Long idCliente;
    private String nombreCompleto;
    private String dpi;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String direccion;
    private Long idUsuario;
    private String emailUsuario;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}