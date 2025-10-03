package com.gestor.gestor_sat.model;

import com.gestor.gestor_sat.model.base.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "archivos", indexes = {
    @Index(name = "idx_archivos_tramites", columnList = "id_tramites"),
    @Index(name = "idx_archivos_cliente", columnList = "id_cliente")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Archivo extends AuditableEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_archivos")
    private Long idArchivos;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tramites", nullable = false)
    private Tramite tramite;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;
    
    @NotBlank(message = "El nombre del archivo es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    @Column(name = "nombre_archivo", nullable = false, length = 50)
    private String nombreArchivo;
    
    @NotBlank(message = "La ruta del archivo es obligatoria")
    @Size(max = 150, message = "La ruta no puede exceder 150 caracteres")
    @Column(nullable = false, length = 150)
    private String ruta;
    
    @Column(name = "fecha_subida", nullable = false)
    @Builder.Default
    private LocalDateTime fechaSubida = LocalDateTime.now();
    
    @Size(max = 100)
    @Column(name = "tipo_mime", length = 100)
    private String tipoMime;
    
    @Min(value = 1, message = "El tamaño debe ser mayor a 0")
    @Column(name = "tamano_bytes")
    private Long tamanoBytes;
}