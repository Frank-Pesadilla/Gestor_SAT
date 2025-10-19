package com.gestor.gestor_sat.mapper;

import com.gestor.gestor_sat.dto.DatosPlataformaDTO;
import com.gestor.gestor_sat.entity.Datos;
import com.gestor.gestor_sat.util.AESEncryptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre Datos entity y DatosPlataformaDTO
 * CU-SAT006: Gestión de Contraseñas por Plataforma - Tarea 10
 *
 * SEGURIDAD:
 * - Encripta contraseñas ANTES de mapear a entidad (para guardar en BD)
 * - Desencripta contraseñas DESPUÉS de mapear a DTO (para retornar al cliente)
 * - Usa AESEncryptionService con AES-256-CBC e IV aleatorio
 * - NUNCA loguea contraseñas en texto plano
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DatosMapper {

    private final AESEncryptionService encryptionService;

    /**
     * Convierte DatosPlataformaDTO a entidad Datos
     * CU-SAT006 - Tarea 5: ENCRIPTA todas las contraseñas ANTES de guardar en BD
     *
     * FLUJO:
     * 1. Recibe DTO con contraseñas en texto plano
     * 2. Encripta cada contraseña con AES-256
     * 3. Mapea a entidad con contraseñas encriptadas
     * 4. Retorna entidad lista para guardar en BD
     *
     * @param dto DTO con contraseñas en texto plano
     * @return Entidad Datos con contraseñas encriptadas
     */
    public Datos toEntity(DatosPlataformaDTO dto) {
        if (dto == null) {
            return null;
        }

        log.debug("Mapeando DTO a entidad Datos - ID Cliente: {}", dto.getIdCliente());

        // Crear entidad base
        Datos datos = Datos.builder()
                .idDatos(dto.getIdDatos())
                .nit(dto.getNit())
                .nis(dto.getNis())
                .email(dto.getEmail())
                .dpi(dto.getDpi())
                .cuentaBancaria(dto.getCuentaBancaria())
                .build();

        // Tarea 5: Encriptar contraseñas ANTES de guardar
        // IMPORTANTE: Solo encriptar si la contraseña no es null/vacía
        if (dto.getContrasenaAgenciaVirtual() != null && !dto.getContrasenaAgenciaVirtual().isEmpty()) {
            log.debug("Encriptando contraseña de Agencia Virtual");
            datos.setContrasenaAgenciaVirtual(encryptionService.encrypt(dto.getContrasenaAgenciaVirtual()));
        }

        if (dto.getContrasenaCorreo() != null && !dto.getContrasenaCorreo().isEmpty()) {
            log.debug("Encriptando contraseña de Correo");
            datos.setContrasenaCorreo(encryptionService.encrypt(dto.getContrasenaCorreo()));
        }

        if (dto.getContrasenaCgc() != null && !dto.getContrasenaCgc().isEmpty()) {
            log.debug("Encriptando contraseña de CGC");
            datos.setContrasenaCgc(encryptionService.encrypt(dto.getContrasenaCgc()));
        }

        if (dto.getContrasenaConsultaGeneral() != null && !dto.getContrasenaConsultaGeneral().isEmpty()) {
            log.debug("Encriptando contraseña de Consulta General");
            datos.setContrasenaConsultaGeneral(encryptionService.encrypt(dto.getContrasenaConsultaGeneral()));
        }

        if (dto.getContrasenaRegahe() != null && !dto.getContrasenaRegahe().isEmpty()) {
            log.debug("Encriptando contraseña de REGAHE");
            datos.setContrasenaRegahe(encryptionService.encrypt(dto.getContrasenaRegahe()));
        }

        log.debug("Mapeo completado - Contraseñas encriptadas exitosamente");

        return datos;
    }

    /**
     * Convierte entidad Datos a DatosPlataformaDTO
     * CU-SAT006 - Tarea 6: DESENCRIPTA contraseñas DESPUÉS de leer de BD
     *
     * FLUJO:
     * 1. Recibe entidad con contraseñas encriptadas de BD
     * 2. Desencripta cada contraseña con AES-256
     * 3. Mapea a DTO con contraseñas en texto plano
     * 4. Retorna DTO listo para enviar al cliente
     *
     * SEGURIDAD:
     * - Solo debe llamarse después de validar autorización (Tarea 7)
     * - Solo el propietario puede ver sus contraseñas
     *
     * @param datos Entidad Datos con contraseñas encriptadas
     * @return DTO con contraseñas desencriptadas (texto plano)
     */
    public DatosPlataformaDTO toDTO(Datos datos) {
        if (datos == null) {
            return null;
        }

        log.debug("Mapeando entidad Datos a DTO - ID Datos: {}", datos.getIdDatos());

        // Crear DTO base
        DatosPlataformaDTO dto = DatosPlataformaDTO.builder()
                .idDatos(datos.getIdDatos())
                .idCliente(datos.getCliente() != null ? datos.getCliente().getIdCliente() : null)
                .nit(datos.getNit())
                .nis(datos.getNis())
                .email(datos.getEmail())
                .dpi(datos.getDpi())
                .cuentaBancaria(datos.getCuentaBancaria())
                .build();

        // Tarea 6: Desencriptar contraseñas DESPUÉS de leer de BD
        // IMPORTANTE: Solo desencriptar si la contraseña no es null/vacía
        try {
            if (datos.getContrasenaAgenciaVirtual() != null && !datos.getContrasenaAgenciaVirtual().isEmpty()) {
                log.debug("Desencriptando contraseña de Agencia Virtual");
                dto.setContrasenaAgenciaVirtual(encryptionService.decrypt(datos.getContrasenaAgenciaVirtual()));
            }

            if (datos.getContrasenaCorreo() != null && !datos.getContrasenaCorreo().isEmpty()) {
                log.debug("Desencriptando contraseña de Correo");
                dto.setContrasenaCorreo(encryptionService.decrypt(datos.getContrasenaCorreo()));
            }

            if (datos.getContrasenaCgc() != null && !datos.getContrasenaCgc().isEmpty()) {
                log.debug("Desencriptando contraseña de CGC");
                dto.setContrasenaCgc(encryptionService.decrypt(datos.getContrasenaCgc()));
            }

            if (datos.getContrasenaConsultaGeneral() != null && !datos.getContrasenaConsultaGeneral().isEmpty()) {
                log.debug("Desencriptando contraseña de Consulta General");
                dto.setContrasenaConsultaGeneral(encryptionService.decrypt(datos.getContrasenaConsultaGeneral()));
            }

            if (datos.getContrasenaRegahe() != null && !datos.getContrasenaRegahe().isEmpty()) {
                log.debug("Desencriptando contraseña de REGAHE");
                dto.setContrasenaRegahe(encryptionService.decrypt(datos.getContrasenaRegahe()));
            }

            log.debug("Mapeo completado - Contraseñas desencriptadas exitosamente");

        } catch (Exception e) {
            log.error("Error al desencriptar contraseñas: {}", e.getMessage());
            throw new RuntimeException("Error al procesar contraseñas", e);
        }

        return dto;
    }

    /**
     * Actualiza una entidad Datos existente con datos de un DTO
     * Útil para operaciones de actualización
     *
     * @param entity Entidad existente a actualizar
     * @param dto DTO con nuevos valores
     */
    public void updateEntity(Datos entity, DatosPlataformaDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        log.debug("Actualizando entidad Datos - ID: {}", entity.getIdDatos());

        // Actualizar datos básicos
        if (dto.getNit() != null) {
            entity.setNit(dto.getNit());
        }
        if (dto.getNis() != null) {
            entity.setNis(dto.getNis());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getDpi() != null) {
            entity.setDpi(dto.getDpi());
        }
        if (dto.getCuentaBancaria() != null) {
            entity.setCuentaBancaria(dto.getCuentaBancaria());
        }

        // Actualizar contraseñas encriptadas si se proporcionan
        if (dto.getContrasenaAgenciaVirtual() != null && !dto.getContrasenaAgenciaVirtual().isEmpty()) {
            entity.setContrasenaAgenciaVirtual(encryptionService.encrypt(dto.getContrasenaAgenciaVirtual()));
        }
        if (dto.getContrasenaCorreo() != null && !dto.getContrasenaCorreo().isEmpty()) {
            entity.setContrasenaCorreo(encryptionService.encrypt(dto.getContrasenaCorreo()));
        }
        if (dto.getContrasenaCgc() != null && !dto.getContrasenaCgc().isEmpty()) {
            entity.setContrasenaCgc(encryptionService.encrypt(dto.getContrasenaCgc()));
        }
        if (dto.getContrasenaConsultaGeneral() != null && !dto.getContrasenaConsultaGeneral().isEmpty()) {
            entity.setContrasenaConsultaGeneral(encryptionService.encrypt(dto.getContrasenaConsultaGeneral()));
        }
        if (dto.getContrasenaRegahe() != null && !dto.getContrasenaRegahe().isEmpty()) {
            entity.setContrasenaRegahe(encryptionService.encrypt(dto.getContrasenaRegahe()));
        }

        log.debug("Actualización completada");
    }
}
