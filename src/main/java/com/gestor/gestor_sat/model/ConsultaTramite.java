package com.gestor.gestor_sat.model;

import com.gestor.gestor_sat.model.base.AuditableEntity;
import com.gestor.gestor_sat.model.enums.TramiteEstado;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "consulta_tramite", indexes = {
    @Index(name = "idx_consulta_tramite", columnList = "id_tramites"),
    @Index(name = "idx_consulta_cliente", columnList = "id_cliente"),
    @Index(name = "idx_consulta_estado", columnList = "estado"),
    @Index(name = "idx_consulta_fecha", columnList = "fecha_tramite")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaTramite extends AuditableEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta_tramite")
    private Long idConsultaTramite;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tramites", nullable = false)
    private Tramite tramite;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;
    
    @NotNull(message = "La fecha del trámite es obligatoria")
    @Column(name = "fecha_tramite", nullable = false)
    @Builder.Default
    private LocalDate fechaTramite = LocalDate.now();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "tramite_estado")
    @Builder.Default
    private TramiteEstado estado = TramiteEstado.INICIADO;
}