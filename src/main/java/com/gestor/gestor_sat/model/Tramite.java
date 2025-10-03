package com.gestor.gestor_sat.model;

import com.gestor.gestor_sat.model.base.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tramites", indexes = {
    @Index(name = "idx_tramites_tipo", columnList = "id_tipo_tramite"),
    @Index(name = "idx_tramites_nombre", columnList = "nombre")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tramite extends AuditableEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tramites")
    private Long idTramites;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_tramite", nullable = false)
    private TipoTramite tipoTramite;
    
    @NotBlank(message = "El nombre del trámite es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    @Column(nullable = false, length = 50)
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @OneToMany(mappedBy = "tramite", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<ConsultaTramite> consultas = new ArrayList<>();
    
    @OneToMany(mappedBy = "tramite", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<Archivo> archivos = new ArrayList<>();
}