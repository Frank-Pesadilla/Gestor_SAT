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
public class TramiteResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long idTipoTramite;
    private String nombreTipoTramite;
    private LocalDateTime fechaCreacion;
}