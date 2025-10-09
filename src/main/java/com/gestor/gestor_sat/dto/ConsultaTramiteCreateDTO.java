package com.gestor.gestor_sat.dto;

import com.gestor.gestor_sat.entity.enums.TramiteEstado;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO para crear consulta de trámite
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaTramiteCreateDTO {

    @NotNull(message = "El ID del trámite es obligatorio")
    private Long idTramites;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long idCliente;

    private LocalDate fechaTramite;

    private TramiteEstado estado;
}