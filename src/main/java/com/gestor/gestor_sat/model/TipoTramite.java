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
@Table(name = "tipo_tramite")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoTramite extends AuditableEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_tramite")
    private Long idTipoTramite;
    
    @NotBlank(message = "El portal es obligatorio")
    @Size(max = 20, message = "El portal no puede exceder 20 caracteres")
    @Column(nullable = false, length = 20)
    private String portal;
    
    @Column(columnDefinition = "TEXT")
    private String link;
    
    @OneToMany(mappedBy = "tipoTramite")
    @Builder.Default
    @JsonIgnore
    private List<Tramite> tramites = new ArrayList<>();
}