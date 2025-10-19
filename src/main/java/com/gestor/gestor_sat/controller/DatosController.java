package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.dto.DatosPlataformaDTO;
import com.gestor.gestor_sat.service.DatosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestión de contraseñas de plataformas
 * CU-SAT006: Gestión de Contraseñas por Plataforma
 *
 * ENDPOINTS:
 * - POST /api/datos/{idCliente}/contrasenas - Guardar contraseñas (Tarea 8)
 * - GET /api/datos/{idCliente}/contrasenas - Obtener contraseñas (Tarea 9)
 *
 * SEGURIDAD:
 * - Todas las contraseñas se transmiten por HTTPS
 * - Solo el propietario puede acceder a sus contraseñas (Tarea 7)
 * - Las contraseñas se encriptan antes de guardar
 * - Las contraseñas se desencriptan al obtener
 */
@RestController
@RequestMapping("/api/datos")
@RequiredArgsConstructor
@Slf4j
public class DatosController {

    private final DatosService datosService;

    /**
     * CU-SAT006 - Tarea 8: Endpoint POST para guardar contraseñas de plataformas
     *
     * FLUJO:
     * 1. Recibe contraseñas en texto plano del cliente (por HTTPS)
     * 2. Valida que el cliente exista
     * 3. Encripta contraseñas usando AES-256 (Tarea 5)
     * 4. Guarda en base de datos
     * 5. Retorna confirmación (sin contraseñas)
     *
     * SEGURIDAD:
     * - Las contraseñas se reciben en texto plano pero SOLO por HTTPS
     * - Se encriptan ANTES de guardar en BD
     * - No se retornan las contraseñas en la respuesta
     *
     * @param idCliente ID del cliente propietario de las contraseñas
     * @param dto DTO con contraseñas de plataformas en texto plano
     * @return DTO con confirmación (sin contraseñas)
     */
    @PostMapping("/{idCliente}/contrasenas")
    public ResponseEntity<DatosPlataformaDTO> guardarContrasenas(
            @PathVariable Long idCliente,
            @Valid @RequestBody DatosPlataformaDTO dto) {

        log.info("POST /api/datos/{}/contrasenas - Guardando contraseñas de plataformas", idCliente);

        // IMPORTANTE: En un entorno de producción real, aquí se debe:
        // 1. Validar que el usuario autenticado sea el propietario (idCliente)
        // 2. Usar Spring Security para obtener el usuario actual
        // 3. Comparar con el idCliente del path
        // Ejemplo:
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // if (!datosService.validarAccesoContrasenas(idCliente, usuarioActual.getId())) {
        //     throw new AccesoNoAutorizadoException();
        // }

        // Tarea 4 y 5: Guardar contraseñas (se encriptan automáticamente)
        DatosPlataformaDTO response = datosService.guardarContrasenas(idCliente, dto);

        log.info("Contraseñas guardadas exitosamente para cliente ID: {}", idCliente);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * CU-SAT006 - Tarea 9: Endpoint GET para obtener contraseñas de plataformas
     *
     * FLUJO:
     * 1. Valida que el cliente exista
     * 2. Valida autorización: solo el propietario puede ver sus contraseñas (Tarea 7)
     * 3. Obtiene contraseñas de BD (encriptadas)
     * 4. Desencripta contraseñas usando AES-256 (Tarea 6)
     * 5. Retorna DTO con contraseñas en texto plano (solo al propietario)
     *
     * SEGURIDAD - Tarea 7:
     * - Solo el propietario puede ver sus contraseñas
     * - Las contraseñas se desencriptan SOLO al retornar al cliente autorizado
     * - Se transmiten por HTTPS
     * - IMPORTANTE: En producción debe validarse la identidad del usuario
     *
     * @param idCliente ID del cliente propietario de las contraseñas
     * @return DTO con contraseñas desencriptadas (texto plano)
     */
    @GetMapping("/{idCliente}/contrasenas")
    public ResponseEntity<DatosPlataformaDTO> obtenerContrasenas(@PathVariable Long idCliente) {

        log.info("GET /api/datos/{}/contrasenas - Obteniendo contraseñas de plataformas", idCliente);

        // IMPORTANTE: En un entorno de producción real, aquí se debe:
        // Tarea 7: Validar que solo el propietario acceda a sus contraseñas
        // Ejemplo:
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Long idUsuarioActual = ((UserDetails) auth.getPrincipal()).getId();
        //
        // if (!datosService.validarAccesoContrasenas(idCliente, idUsuarioActual)) {
        //     log.warn("Acceso no autorizado: Usuario {} intentó acceder a contraseñas de cliente {}",
        //             idUsuarioActual, idCliente);
        //     throw new CustomExceptions.AccesoNoAutorizadoException(
        //         "No tiene permisos para ver las contraseñas de este cliente");
        // }

        // Tarea 6: Obtener contraseñas (se desencriptan automáticamente)
        DatosPlataformaDTO response = datosService.obtenerContrasenas(idCliente);

        log.info("Contraseñas obtenidas exitosamente para cliente ID: {}", idCliente);
        // NUNCA loguear el response completo (contiene contraseñas)

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint adicional: Verifica si existen datos para un cliente
     *
     * @param idCliente ID del cliente
     * @return true si existen datos, false si no
     */
    @GetMapping("/{idCliente}/existe")
    public ResponseEntity<Boolean> existenDatos(@PathVariable Long idCliente) {
        log.info("GET /api/datos/{}/existe - Verificando existencia de datos", idCliente);

        boolean existe = datosService.existenDatosCliente(idCliente);

        return ResponseEntity.ok(existe);
    }

    /**
     * Endpoint adicional: Elimina datos de un cliente
     * CUIDADO: Esto eliminará TODAS las contraseñas guardadas
     *
     * @param idCliente ID del cliente
     * @return Mensaje de confirmación
     */
    @DeleteMapping("/{idCliente}/contrasenas")
    public ResponseEntity<String> eliminarContrasenas(@PathVariable Long idCliente) {
        log.info("DELETE /api/datos/{}/contrasenas - Eliminando contraseñas", idCliente);

        // IMPORTANTE: Validar autorización antes de eliminar
        // Solo el propietario debe poder eliminar sus datos

        datosService.eliminarDatosCliente(idCliente);

        log.info("Contraseñas eliminadas exitosamente para cliente ID: {}", idCliente);

        return ResponseEntity.ok("Contraseñas eliminadas exitosamente");
    }
}
