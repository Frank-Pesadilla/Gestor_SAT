package com.gestor.gestor_sat.mapper;

import com.gestor.gestor_sat.dto.ArchivoHistorialDTO;
import com.gestor.gestor_sat.dto.HistorialTramiteDTO;
import com.gestor.gestor_sat.entity.Archivo;
import com.gestor.gestor_sat.entity.ConsultaTramite;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir ConsultaTramite a HistorialTramiteDTO
 * CU-SAT005: Historial de Trámites
 */
@Component
public class HistorialTramiteMapper {

    /**
     * Convierte una ConsultaTramite con sus archivos asociados a HistorialTramiteDTO
     * Incluye toda la información del trámite, tipo, cliente y archivos
     *
     * @param consultaTramite Consulta de trámite a convertir
     * @param archivos Lista de archivos asociados al trámite
     * @return DTO con toda la información del historial
     */
    public HistorialTramiteDTO toHistorialDTO(ConsultaTramite consultaTramite, List<Archivo> archivos) {
        return HistorialTramiteDTO.builder()
                // Información de la consulta
                .idConsultaTramite(consultaTramite.getIdConsultaTramite())
                .fechaTramite(consultaTramite.getFechaTramite())
                .estadoActual(consultaTramite.getEstado())
                .estadoDescripcion(consultaTramite.getEstado().getDescripcion())

                // Información del trámite
                .idTramite(consultaTramite.getTramite().getIdTramites())
                .nombreTramite(consultaTramite.getTramite().getNombre())
                .descripcionTramite(consultaTramite.getTramite().getDescripcion())

                // Información del tipo de trámite
                .idTipoTramite(consultaTramite.getTramite().getTipoTramite().getIdTipoTramite())
                .portal(consultaTramite.getTramite().getTipoTramite().getPortal())
                .linkPortal(consultaTramite.getTramite().getTipoTramite().getLink())

                // Información del cliente
                .idCliente(consultaTramite.getCliente().getIdCliente())
                .nombreCliente(consultaTramite.getCliente().getNombreCompleto())
                .dpiCliente(consultaTramite.getCliente().getDpi())

                // Tarea 4: Lista de archivos adjuntos
                .archivosAdjuntos(archivos.stream()
                        .map(this::toArchivoHistorialDTO)
                        .collect(Collectors.toList()))

                // Fechas de auditoría
                .createdAt(consultaTramite.getCreatedAt())
                .updatedAt(consultaTramite.getUpdatedAt())
                .build();
    }

    /**
     * Convierte un Archivo a ArchivoHistorialDTO
     * Tarea 4: Incluye información de archivos (nombre, ruta, tipo, fecha)
     *
     * @param archivo Entidad Archivo
     * @return DTO con información del archivo
     */
    private ArchivoHistorialDTO toArchivoHistorialDTO(Archivo archivo) {
        return ArchivoHistorialDTO.builder()
                .idArchivo(archivo.getIdArchivos())
                .nombreArchivo(archivo.getNombreArchivo())
                .ruta(archivo.getRuta())
                .fechaSubida(archivo.getFechaSubida())
                .build();
    }
}
