Notificacion.java
package com.gestor.gestor_sat.model;

import com.gestor.gestor_sat.model.base.AuditableEntity;
import com.gestor.gestor_sat.model.enums.TipoNotificacion;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Notificacion extends AuditableEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoNotificacion tipo;
    
    @Column(nullable = false)
    private Boolean leida = false;
    
    @Column(name = "fecha_lectura")
    private java.time.LocalDateTime fechaLectura;
}
