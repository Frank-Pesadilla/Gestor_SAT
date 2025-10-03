package com.gestor.gestor_sat.model;

import com.gestor.gestor_sat.model.base.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "datos", uniqueConstraints = {
    @UniqueConstraint(name = "idx_datos_cliente_unique", columnNames = "id_cliente")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Datos extends AuditableEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_datos")
    private Long idDatos;
    
    @OneToOne
    @JoinColumn(name = "id_cliente", nullable = false, unique = true)
    private Cliente cliente;
    
    @Pattern(regexp = "^[0-9]{1,8}-[0-9K]$", message = "Formato de NIT inválido")
    @Column(length = 15)
    private String nit;
    
    @Column
    private Integer nis;
    
    @Column(columnDefinition = "CITEXT")
    private String email;
    
    @Pattern(regexp = "^[0-9]{13}$", message = "El DPI debe tener 13 dígitos")
    @Column(length = 13)
    private String dpi;
    
    @Column(name = "cuenta_bancaria", length = 20)
    private String cuentaBancaria;
    
    // Contraseñas ENCRIPTADAS con AES-256 (almacenadas en Base64)
    @Column(name = "contrasena_agencia_virtual", columnDefinition = "TEXT")
    private String contrasenaAgenciaVirtual;
    
    @Column(name = "contrasena_correo", columnDefinition = "TEXT")
    private String contrasenaCorreo;
    
    @Column(name = "contrasena_cgc", columnDefinition = "TEXT")
    private String contrasenaCgc;
    
    @Column(name = "contrasena_consulta_general", columnDefinition = "TEXT")
    private String contrasenaConsultaGeneral;
    
    @Column(name = "contrasena_regahe", columnDefinition = "TEXT")
    private String contrasenaRegahe;
}