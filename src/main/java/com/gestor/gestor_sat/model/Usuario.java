package com.gestor.gestor_sat.model;

import com.gestor.gestor_sat.model.base.AuditableEntity;
import com.gestor.gestor_sat.model.enums.UsuarioEstado;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "usuario", indexes = {
    @Index(name = "idx_usuario_email", columnList = "email"),
    @Index(name = "idx_usuario_estado", columnList = "estado")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario extends AuditableEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;
    
    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 150, message = "El usuario no puede exceder 150 caracteres")
    @Column(nullable = false, unique = true, length = 150)
    private String usuario;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    @Column(nullable = false, unique = true, columnDefinition = "CITEXT")
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "usuario_estado")
    @Builder.Default
    private UsuarioEstado estado = UsuarioEstado.ACTIVO;
    
    // Relación One-to-One con Cliente
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cliente cliente;
}