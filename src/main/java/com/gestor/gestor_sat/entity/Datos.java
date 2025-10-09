package com.gestor.gestor_sat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "datos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Datos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_datos")
    private Long idDatos;

    @OneToOne
    @JoinColumn(name = "id_cliente", nullable = false, unique = true)
    private Cliente cliente;

    @Column(name = "nit", length = 15)
    private String nit;

    @Column(name = "nis")
    private Integer nis;

    @Column(name = "email")
    private String email;

    @Column(name = "dpi", length = 13)
    private String dpi;

    @Column(name = "cuenta_bancaria", length = 20)
    private String cuentaBancaria;

    // Contraseñas encriptadas con AES-256
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