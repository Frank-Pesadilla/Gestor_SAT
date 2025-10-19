package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.dto.RecuperacionResponseDTO;
import com.gestor.gestor_sat.dto.RecuperarContrasenaRequestDTO;
import com.gestor.gestor_sat.dto.RestablecerContrasenaRequestDTO;
import com.gestor.gestor_sat.service.security.PasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

/**
 * Controlador REST para autenticación y recuperación de contraseñas
 * CU-SAT013: Recuperación de Contraseña
 *
 * Endpoints:
 * - POST /api/auth/recuperar-contrasena - Solicitar recuperación
 * - POST /api/auth/restablecer-contrasena - Restablecer con token
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final PasswordService passwordService;

    /**
     * TAREA 10: Endpoint para solicitar recuperación de contraseña
     *
     * FLUJO:
     * 1. Recibe email del usuario
     * 2. Genera token de recuperación (si el email existe)
     * 3. Envía email con link de recuperación (si el email existe)
     * 4. Retorna SIEMPRE el mismo mensaje por seguridad
     *
     * SEGURIDAD IMPORTANTE:
     * - NO revela si el email existe o no en el sistema
     * - Siempre retorna el mismo mensaje de éxito
     * - Esto previene ataques de enumeración de usuarios
     *
     * @param request DTO con el email del usuario
     * @return Mensaje genérico de éxito (siempre el mismo)
     */
    @PostMapping("/recuperar-contrasena")
    public ResponseEntity<RecuperacionResponseDTO> recuperarContrasena(
            @Valid @RequestBody RecuperarContrasenaRequestDTO request) {

        log.info("POST /api/auth/recuperar-contrasena - Solicitud de recuperación recibida");

        String email = request.getEmail();

        try {
            // Generar token de recuperación
            Optional<UUID> tokenOpt = passwordService.generarTokenRecuperacion(email);

            // Si el email existe, enviar el correo
            if (tokenOpt.isPresent()) {
                UUID token = tokenOpt.get();
                passwordService.enviarEmailRecuperacion(email, token);
                log.info("Token generado y email enviado para: {}", email);
            } else {
                // Email no existe, pero NO revelamos esta información al cliente
                log.info("Email no encontrado, pero se retorna mensaje genérico por seguridad");
            }

            // SEGURIDAD: SIEMPRE retornar el mismo mensaje
            // No revelar si el email existe o no
            RecuperacionResponseDTO response = RecuperacionResponseDTO.builder()
                    .mensaje("Si el email existe en nuestro sistema, recibirás un correo con instrucciones para recuperar tu contraseña")
                    .exito(true)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error en recuperación de contraseña: {}", e.getMessage(), e);

            // En caso de error, retornar mensaje genérico sin detalles técnicos
            RecuperacionResponseDTO response = RecuperacionResponseDTO.builder()
                    .mensaje("Ocurrió un error al procesar tu solicitud. Intenta nuevamente más tarde")
                    .exito(false)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * TAREA 11: Endpoint para restablecer contraseña con token
     *
     * FLUJO:
     * 1. Recibe token UUID y nueva contraseña
     * 2. Valida el token (existe, no expirado, no usado)
     * 3. Hashea la nueva contraseña con BCrypt
     * 4. Actualiza la contraseña del usuario
     * 5. Marca el token como usado
     *
     * VALIDACIONES:
     * - Token debe ser UUID válido
     * - Token debe existir en la base de datos
     * - Token no debe estar expirado (< 1 hora)
     * - Token no debe haber sido usado previamente
     * - Contraseña debe tener mínimo 8 caracteres
     *
     * @param request DTO con token y nueva contraseña
     * @return Mensaje de éxito o error
     */
    @PostMapping("/restablecer-contrasena")
    public ResponseEntity<RecuperacionResponseDTO> restablecerContrasena(
            @Valid @RequestBody RestablecerContrasenaRequestDTO request) {

        log.info("POST /api/auth/restablecer-contrasena - Intento de restablecimiento de contraseña");

        try {
            // Convertir token String a UUID
            UUID token = request.getTokenAsUUID();

            // Restablecer contraseña
            boolean exito = passwordService.restablecerContrasena(token, request.getNuevaContrasena());

            if (exito) {
                log.info("Contraseña restablecida exitosamente");

                RecuperacionResponseDTO response = RecuperacionResponseDTO.builder()
                        .mensaje("Tu contraseña ha sido restablecida exitosamente. Ya puedes iniciar sesión con tu nueva contraseña")
                        .exito(true)
                        .build();

                return ResponseEntity.ok(response);

            } else {
                log.warn("Token inválido, expirado o ya usado");

                RecuperacionResponseDTO response = RecuperacionResponseDTO.builder()
                        .mensaje("El token es inválido, ha expirado o ya fue utilizado. Solicita un nuevo enlace de recuperación")
                        .exito(false)
                        .build();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        } catch (IllegalArgumentException e) {
            log.warn("Error de validación: {}", e.getMessage());

            RecuperacionResponseDTO response = RecuperacionResponseDTO.builder()
                    .mensaje(e.getMessage())
                    .exito(false)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            log.error("Error al restablecer contraseña: {}", e.getMessage(), e);

            RecuperacionResponseDTO response = RecuperacionResponseDTO.builder()
                    .mensaje("Ocurrió un error al restablecer tu contraseña. Intenta nuevamente más tarde")
                    .exito(false)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint adicional para validar si un token es válido
     * Útil para el frontend para verificar el token antes de mostrar el formulario
     *
     * @param token Token UUID a validar
     * @return true si el token es válido, false si no
     */
    @GetMapping("/validar-token/{token}")
    public ResponseEntity<RecuperacionResponseDTO> validarToken(@PathVariable String token) {

        log.info("GET /api/auth/validar-token/{} - Validando token", token);

        try {
            UUID tokenUUID = UUID.fromString(token);
            boolean esValido = passwordService.validarToken(tokenUUID);

            RecuperacionResponseDTO response = RecuperacionResponseDTO.builder()
                    .mensaje(esValido ? "Token válido" : "Token inválido, expirado o ya usado")
                    .exito(esValido)
                    .build();

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            RecuperacionResponseDTO response = RecuperacionResponseDTO.builder()
                    .mensaje("Token con formato inválido")
                    .exito(false)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
