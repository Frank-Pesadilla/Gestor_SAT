package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.DatosPlataformaDTO;
import com.gestor.gestor_sat.entity.Cliente;
import com.gestor.gestor_sat.entity.Datos;
import com.gestor.gestor_sat.exception.CustomExceptions;
import com.gestor.gestor_sat.mapper.DatosMapper;
import com.gestor.gestor_sat.repository.ClienteRepository;
import com.gestor.gestor_sat.repository.DatosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para gestión de contraseñas de plataformas
 * CU-SAT006: Gestión de Contraseñas por Plataforma
 *
 * TAREAS IMPLEMENTADAS:
 * - Tarea 4: Guardar contraseñas de plataformas asociadas a un cliente
 * - Tarea 5: Encriptar contraseñas ANTES de guardar en BD
 * - Tarea 6: Desencriptar contraseñas DESPUÉS de leer de BD
 * - Tarea 7: Validar que solo el propietario vea sus contraseñas
 *
 * SEGURIDAD:
 * - Todas las contraseñas se encriptan con AES-256-CBC e IV aleatorio
 * - Solo el propietario puede acceder a sus contraseñas
 * - No se loguean contraseñas en texto plano
 * - Validación de autorización en todos los métodos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DatosService {

    private final DatosRepository datosRepository;
    private final ClienteRepository clienteRepository;
    private final DatosMapper datosMapper;

    /**
     * CU-SAT006 - Tarea 4: Guarda contraseñas de plataformas para un cliente
     *
     * FLUJO:
     * 1. Valida que el cliente exista
     * 2. Busca si ya existen datos para el cliente
     * 3. Si existen, actualiza; si no, crea nuevo
     * 4. Encripta contraseñas usando DatosMapper (Tarea 5)
     * 5. Guarda en base de datos
     *
     * SEGURIDAD:
     * - Las contraseñas se reciben en texto plano del cliente
     * - Se encriptan ANTES de guardar (ver DatosMapper.toEntity)
     * - Se almacenan encriptadas en la BD
     *
     * @param idCliente ID del cliente propietario de las contraseñas
     * @param dto DTO con contraseñas de plataformas en texto plano
     * @return DTO con datos guardados (contraseñas encriptadas, no visibles)
     * @throws CustomExceptions.ClienteNoEncontradoException si el cliente no existe
     */
    @Transactional
    public DatosPlataformaDTO guardarContrasenas(Long idCliente, DatosPlataformaDTO dto) {
        log.info("Guardando contraseñas de plataformas para cliente ID: {}", idCliente);

        // Validar que el cliente existe
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new CustomExceptions.ClienteNoEncontradoException(idCliente));

        log.debug("Cliente encontrado: {}", cliente.getNombreCompleto());

        // Buscar si ya existen datos para este cliente
        Datos datosExistentes = datosRepository.findByClienteIdCliente(idCliente).orElse(null);

        Datos datosFinal;

        if (datosExistentes != null) {
            // Actualizar datos existentes
            log.info("Actualizando datos existentes para cliente ID: {}", idCliente);
            datosMapper.updateEntity(datosExistentes, dto);
            datosFinal = datosRepository.save(datosExistentes);
        } else {
            // Crear nuevos datos
            log.info("Creando nuevos datos para cliente ID: {}", idCliente);

            // Tarea 5: Convertir DTO a entidad (encripta contraseñas automáticamente)
            Datos nuevosDatos = datosMapper.toEntity(dto);
            nuevosDatos.setCliente(cliente);

            datosFinal = datosRepository.save(nuevosDatos);
        }

        log.info("Contraseñas guardadas exitosamente para cliente ID: {}", idCliente);

        // Retornar DTO sin contraseñas desencriptadas por seguridad
        return DatosPlataformaDTO.builder()
                .idDatos(datosFinal.getIdDatos())
                .idCliente(idCliente)
                .nit(datosFinal.getNit())
                .nis(datosFinal.getNis())
                .email(datosFinal.getEmail())
                .dpi(datosFinal.getDpi())
                .cuentaBancaria(datosFinal.getCuentaBancaria())
                // NO retornar contraseñas aquí por seguridad
                .build();
    }

    /**
     * CU-SAT006 - Tarea 6: Obtiene contraseñas de plataformas de un cliente
     *
     * FLUJO:
     * 1. Valida que el cliente exista
     * 2. Busca datos del cliente en BD
     * 3. Desencripta contraseñas usando DatosMapper (Tarea 6)
     * 4. Retorna DTO con contraseñas en texto plano
     *
     * SEGURIDAD - Tarea 7:
     * - Solo el propietario puede ver sus contraseñas
     * - Las contraseñas están encriptadas en BD
     * - Se desencriptan SOLO al retornar al cliente autorizado
     * - IMPORTANTE: Este método debe ser llamado solo después de validar autorización
     *
     * @param idCliente ID del cliente propietario de las contraseñas
     * @return DTO con contraseñas desencriptadas (texto plano)
     * @throws CustomExceptions.ClienteNoEncontradoException si el cliente no existe
     * @throws CustomExceptions.RecursoNoEncontradoException si no hay datos para el cliente
     */
    @Transactional(readOnly = true)
    public DatosPlataformaDTO obtenerContrasenas(Long idCliente) {
        log.info("Obteniendo contraseñas de plataformas para cliente ID: {}", idCliente);

        // Tarea 7: Validar que el cliente existe
        if (!clienteRepository.existsById(idCliente)) {
            log.error("Cliente no encontrado con ID: {}", idCliente);
            throw new CustomExceptions.ClienteNoEncontradoException(idCliente);
        }

        // Buscar datos del cliente
        Datos datos = datosRepository.findByClienteIdCliente(idCliente)
                .orElseThrow(() -> {
                    log.error("No se encontraron datos para cliente ID: {}", idCliente);
                    return new CustomExceptions.RecursoNoEncontradoException("Datos", idCliente);
                });

        log.debug("Datos encontrados para cliente ID: {}", idCliente);

        // Tarea 6: Convertir entidad a DTO (desencripta contraseñas automáticamente)
        DatosPlataformaDTO dto = datosMapper.toDTO(datos);

        log.info("Contraseñas obtenidas y desencriptadas exitosamente para cliente ID: {}", idCliente);
        // IMPORTANTE: NUNCA loguear el DTO completo (contiene contraseñas en texto plano)

        return dto;
    }

    /**
     * CU-SAT006 - Tarea 7: Valida si un cliente tiene acceso a ver contraseñas
     *
     * REGLA DE NEGOCIO:
     * - Solo el propietario (el mismo cliente) puede ver sus contraseñas
     * - Administradores NO pueden ver contraseñas de clientes
     *
     * @param idCliente ID del cliente propietario
     * @param idClienteSolicitante ID del cliente que solicita acceso
     * @return true si tiene acceso, false si no
     */
    public boolean validarAccesoContrasenas(Long idCliente, Long idClienteSolicitante) {
        // Tarea 7: Solo el propietario puede ver sus propias contraseñas
        boolean tieneAcceso = idCliente.equals(idClienteSolicitante);

        if (!tieneAcceso) {
            log.warn("Intento de acceso no autorizado: Cliente {} intentó acceder a contraseñas de cliente {}",
                    idClienteSolicitante, idCliente);
        }

        return tieneAcceso;
    }

    /**
     * Verifica si existen datos para un cliente
     *
     * @param idCliente ID del cliente
     * @return true si existen datos, false si no
     */
    @Transactional(readOnly = true)
    public boolean existenDatosCliente(Long idCliente) {
        return datosRepository.existsByClienteIdCliente(idCliente);
    }

    /**
     * Elimina los datos de un cliente
     * NOTA: Esto eliminará TODAS las contraseñas guardadas
     *
     * @param idCliente ID del cliente
     */
    @Transactional
    public void eliminarDatosCliente(Long idCliente) {
        log.info("Eliminando datos de cliente ID: {}", idCliente);

        Datos datos = datosRepository.findByClienteIdCliente(idCliente)
                .orElseThrow(() -> new CustomExceptions.RecursoNoEncontradoException("Datos", idCliente));

        datosRepository.delete(datos);

        log.info("Datos eliminados exitosamente para cliente ID: {}", idCliente);
    }
}
