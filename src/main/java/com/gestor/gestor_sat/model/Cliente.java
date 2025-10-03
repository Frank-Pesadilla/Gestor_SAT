package com.gestor.gestor_sat.model;

import com.gestor.gestor_sat.model.base.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente", indexes = {
    @Index(name = "idx_cliente_dpi", columnList = "dpi"),
    @Index(name = "idx_cliente_usuario", columnList = "id_usuario")
}, uniqueConstraints = {
    @UniqueConstraint(name = "idx_cliente_usuario_unique", columnNames = "id_usuario")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente extends AuditableEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;
    
    // Relación One-to-One OPCIONAL con Usuario
    @OneToOne
    @JoinColumn(name = "id_usuario", unique = true)
    private Usuario usuario;
    
    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
    @Column(name = "nombre_completo", nullable = false, length = 150)
    private String nombreCompleto;
    
    @Column(columnDefinition = "TEXT")
    private String direccion;
    
    @NotBlank(message = "El DPI es obligatorio")
    @Pattern(regexp = "^[0-9]{13}$", message = "El DPI debe tener exactamente 13 dígitos")
    @Column(nullable = false, unique = true, length = 13)
    private String dpi;
    
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;
    
    @Pattern(regexp = "^[0-9]{8}$", message = "El teléfono debe tener 8 dígitos")
    @Column(length = 8)
    private String telefono;
    
    // Relaciones One-to-Many
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ConsultaTramite> consultas = new ArrayList<>();
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Archivo> archivos = new ArrayList<>();
    
    // Relación One-to-One con Datos
    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Datos datos;
    
    // Validación personalizada
    @AssertTrue(message = "El cliente debe ser mayor de 18 años")
    public boolean isMayorDeEdad() {
        if (fechaNacimiento == null) return false;
        return fechaNacimiento.plusYears(18).isBefore(LocalDate.now()) || 
               fechaNacimiento.plusYears(18).isEqual(LocalDate.now());
    }
}