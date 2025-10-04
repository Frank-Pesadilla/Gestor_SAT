package com.gestor.gestor_sat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArchivoResponseDTO {
    private Long id;
    private String nombreArchivo;
    private String urlDrive;
    private String descripcion;
    private Long idTramite;
    private String nombreTramite;
    private Long idCliente;
    private String nombreCliente;
    private LocalDateTime fechaRegistro;
}