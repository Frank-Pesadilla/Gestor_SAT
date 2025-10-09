package com.gestor.gestor_sat.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO para subir archivos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArchivoUploadDTO {

    @NotNull(message = "El archivo es obligatorio")
    private MultipartFile archivo;

    @NotNull(message = "El ID del trámite es obligatorio")
    private Long idTramite;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long idCliente;
}