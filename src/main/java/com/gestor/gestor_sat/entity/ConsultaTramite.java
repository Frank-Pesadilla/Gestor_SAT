package com.gestor.gestor_sat.entity;

import com.gestor.gestor_sat.entity.enums.TramiteEstado;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "consulta_tramite")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaTramite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta_tramite")
    private Long idConsultaTramite;

    @ManyToOne
    @JoinColumn(name = "id_tramites", nullable = false)
    private Tramite tramite;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Column(name = "fecha_tramite", nullable = false)
    @Builder.Default
    private LocalDate fechaTramite = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "VARCHAR(20)")
    @Builder.Default
    private TramiteEstado estado = TramiteEstado.INICIADO;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 150)
    private String createdBy;

    @Column(name = "updated_by", length = 150)
    private String updatedBy;
}