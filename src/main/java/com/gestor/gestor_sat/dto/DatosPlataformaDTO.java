package com.gestor.gestor_sat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO para gestión de contraseñas de plataformas
 * CU-SAT006: Gestión de Contraseñas por Plataforma - Tarea 3
 *
 * Contiene información de contraseñas de diferentes plataformas gubernamentales:
 * - Agencia Virtual
 * - Correo
 * - CGC (Contraloría General de Cuentas)
 * - Consulta General
 * - REGAHE (Registro de Agentes de Retención de Honduras)
 *
 * SEGURIDAD:
 * - Las contraseñas se envían en texto plano en el request
 * - Se encriptan ANTES de guardar en BD (ver DatosMapper)
 * - Se desencriptan DESPUÉS de leer de BD (ver DatosMapper)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatosPlataformaDTO {

    // Información general del cliente
    private Long idDatos;
    private Long idCliente;

    // Datos adicionales del cliente
    @Size(max = 15, message = "El NIT no puede exceder 15 caracteres")
    private String nit;

    private Integer nis;

    private String email;

    @Size(max = 13, message = "El DPI debe tener exactamente 13 caracteres")
    private String dpi;

    @Size(max = 20, message = "La cuenta bancaria no puede exceder 20 caracteres")
    private String cuentaBancaria;

    // ===============================================================
    // CONTRASEÑAS DE PLATAFORMAS
    // ===============================================================
    // NOTA: Estas contraseñas se reciben en texto plano desde el cliente
    // Se encriptarán con AES-256 antes de guardar en la BD
    // Se desencriptarán al leer de la BD

    /**
     * Contraseña para Agencia Virtual SAT
     * Portal: https://portal.sat.gob.gt/portal/
     */
    private String contrasenaAgenciaVirtual;

    /**
     * Contraseña del correo electrónico del cliente
     */
    private String contrasenaCorreo;

    /**
     * Contraseña para CGC (Contraloría General de Cuentas)
     * Portal: https://sicoin.minfin.gob.gt/
     */
    private String contrasenaCgc;

    /**
     * Contraseña para Consulta General de trámites
     */
    private String contrasenaConsultaGeneral;

    /**
     * Contraseña para REGAHE
     * Sistema de registro de agentes
     */
    private String contrasenaRegahe;

    // ===============================================================
    // NOTAS DE SEGURIDAD
    // ===============================================================
    // - NUNCA loguear este DTO completo (contiene contraseñas)
    // - Las contraseñas deben transmitirse solo por HTTPS
    // - Implementar validación de autorización antes de retornar
    // - Solo el propietario puede ver sus contraseñas
}
