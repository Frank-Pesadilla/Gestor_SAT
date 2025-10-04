package com.gestor.gestor_sat.dto; 
import lombok.AllArgsConstructor; 
import lombok.Builder; 
import lombok.Data; 
import lombok.NoArgsConstructor; 
import java.time.LocalDate; 
import java.util.List; 
@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
public class HistorialTramiteDTO { 
private Long idConsulta; 
private Long idTramite; 
private String nombreTramite; 
private String descripcionTramite; 
private String tipoTramite; 
private String estado; 
private LocalDate fechaCreacion; 
private LocalDate fechaFinalizacion; 
    private String observaciones; 
    private List<ArchivoInfoDTO> archivos; 
     
    @Data 
    @Builder 
    @NoArgsConstructor 
    @AllArgsConstructor 
    public static class ArchivoInfoDTO { 
        private Long id; 
        private String nombreArchivo; 
        private String tipoMime; 
        private Long tamano; 
        private LocalDate fechaSubida; 
    } 
}