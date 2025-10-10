package com.gestor.gestor_sat.dto;

import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BusquedaAvanzadaDTO {
    private String nombre;       // nombreCompleto (cliente)
    private String dpi;          // cliente.dpi
    private LocalDate fechaInicio; // rango para ConsultaTramite.fechaTramite
    private LocalDate fechaFin;
    private String estado;       // INICIADO/EN_PROCESO/FINALIZADO...
}
