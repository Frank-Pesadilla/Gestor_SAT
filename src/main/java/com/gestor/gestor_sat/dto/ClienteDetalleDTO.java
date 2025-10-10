package com.gestor.gestor_sat.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClienteDetalleDTO {
    // Datos básicos del cliente
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

    // Datos “Datos” asociados (tabla datos)
    private String nit;
    private Integer nis;
    private String emailAlterno;
    private String cuentaBancaria;

    // Archivos asociados (resumen)
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ArchivoResumen {
        private Long idArchivos;
        private String nombreArchivo;
        private String url; // ruta
        private LocalDateTime fechaSubida;
    }
    private List<ArchivoResumen> archivos;

    // Trámites asociados (ConsultaTramite)
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class TramiteResumen {
        private Long idConsultaTramite;
        private Long idTramites;
        private String nombreTramite;
        private LocalDate fechaTramite;
        private String estado;
    }
    private List<TramiteResumen> tramites;
}
