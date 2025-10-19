package com.gestor.gestor_sat.service.security;

import com.gestor.gestor_sat.entity.PasswordResetToken;
import com.gestor.gestor_sat.entity.Usuario;
import com.gestor.gestor_sat.repository.PasswordResetTokenRepository;
import com.gestor.gestor_sat.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio para gestión de contraseñas y recuperación
 * CU-SAT013: Recuperación de Contraseña
 *
 * FLUJO DE RECUPERACIÓN:
 * 1. Usuario solicita recuperación con su email
 * 2. Sistema genera token UUID único con expiración de 1 hora
 * 3. Sistema envía email con link de recuperación
 * 4. Usuario hace clic en el link y proporciona nueva contraseña
 * 5. Sistema valida token (existe, no expirado, no usado)
 * 6. Sistema hashea nueva contraseña con BCrypt
 * 7. Sistema actualiza contraseña y marca token como usado
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    // Expiración del token: 1 hora exacta
    private static final long TOKEN_EXPIRATION_HOURS = 1;

    /**
     * TAREA 1: Hashea una contraseña usando BCrypt
     *
     * @param plainPassword Contraseña en texto plano
     * @return Contraseña hasheada con BCrypt (factor >= 10)
     */
    public String hashPassword(String plainPassword) {
        log.debug("Hasheando contraseña con BCrypt");
        return passwordEncoder.encode(plainPassword);
    }

    /**
     * TAREA 2 y TAREA 3: Genera token de recuperación
     *
     * PROCESO:
     * 1. Verifica que el email exista en la base de datos
     * 2. Genera un UUID único como token
     * 3. Crea PasswordResetToken con expiración de 1 hora
     * 4. Asocia el token al usuario
     * 5. Guarda en la base de datos
     *
     * SEGURIDAD (TAREA 5):
     * - No revela si el email existe o no
     * - Retorna Optional.empty() si no existe el email
     *
     * @param email Email del usuario
     * @return Optional con el token generado, empty si el email no existe
     */
    @Transactional
    public Optional<UUID> generarTokenRecuperacion(String email) {
        log.info("Solicitud de recuperación de contraseña para email: {}", email);

        // TAREA 5: Validar que el email existe
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            log.warn("Intento de recuperación para email no existente (no se revela al cliente)");
            return Optional.empty();
        }

        Usuario usuario = usuarioOpt.get();

        // TAREA 3: Generar UUID como token y calcular expiración (1 hora)
        UUID token = UUID.randomUUID();
        ZonedDateTime ahora = ZonedDateTime.now();
        ZonedDateTime expiracion = ahora.plusHours(TOKEN_EXPIRATION_HOURS);

        // Crear y guardar el token
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .usuario(usuario)
                .token(token)
                .createdAt(ahora)
                .expiresAt(expiracion)
                .usedAt(null) // No usado aún
                .build();

        passwordResetTokenRepository.save(resetToken);

        log.info("Token de recuperación generado exitosamente para usuario ID: {} (expira en 1 hora)",
                usuario.getIdUsuario());

        return Optional.of(token);
    }

    /**
     * TAREA 4: Envía email de recuperación con el token
     *
     * El email incluye un link clickeable con el token UUID
     * Formato: http://tudominio.com/restablecer?token={UUID}
     *
     * @param email Email del destinatario
     * @param token Token UUID generado
     */
    public void enviarEmailRecuperacion(String email, UUID token) {
        log.info("Enviando email de recuperación a: {}", email);

        // Construir el link de recuperación
        String linkRecuperacion = "http://tudominio.com/restablecer?token=" + token.toString();

        // Crear mensaje de email
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(email);
        mensaje.setSubject("Recuperación de Contraseña - Gestor SAT");
        mensaje.setText(String.format("""
                Hola,

                Has solicitado recuperar tu contraseña en el sistema Gestor SAT.

                Para restablecer tu contraseña, haz clic en el siguiente enlace:
                %s

                Este enlace expirará en 1 hora por seguridad.

                Si no solicitaste este cambio, ignora este mensaje.

                Saludos,
                Equipo Gestor SAT
                """, linkRecuperacion));

        try {
            mailSender.send(mensaje);
            log.info("Email de recuperación enviado exitosamente a: {}", email);
        } catch (Exception e) {
            log.error("Error al enviar email de recuperación a {}: {}", email, e.getMessage());
            throw new RuntimeException("Error al enviar email de recuperación", e);
        }
    }

    /**
     * TAREA 6: Valida que un token sea válido
     *
     * VALIDACIONES:
     * 1. El token existe en la base de datos
     * 2. El token no ha expirado (menos de 1 hora desde creación)
     * 3. El token no ha sido usado previamente
     *
     * @param token UUID del token a validar
     * @return true si el token es válido, false si no
     */
    @Transactional(readOnly = true)
    public boolean validarToken(UUID token) {
        log.debug("Validando token de recuperación: {}", token);

        Optional<PasswordResetToken> tokenOpt = passwordResetTokenRepository.findByToken(token);

        if (tokenOpt.isEmpty()) {
            log.warn("Token no encontrado: {}", token);
            return false;
        }

        PasswordResetToken resetToken = tokenOpt.get();

        // Usar el método isValid() de la entidad que verifica expiración y uso
        boolean esValido = resetToken.isValid();

        if (!esValido) {
            if (resetToken.isExpired()) {
                log.warn("Token expirado: {}", token);
            }
            if (resetToken.isUsed()) {
                log.warn("Token ya usado: {}", token);
            }
        }

        return esValido;
    }

    /**
     * TAREA 7, TAREA 8 y TAREA 9: Restablece la contraseña de un usuario
     *
     * PROCESO:
     * 1. Valida el token (existe, no expirado, no usado)
     * 2. Hashea la nueva contraseña con BCrypt (NUNCA guardar en texto plano)
     * 3. Actualiza la contraseña del usuario
     * 4. Marca el token como usado (usedAt = now) para prevenir reutilización
     *
     * @param token UUID del token de recuperación
     * @param nuevaContrasena Nueva contraseña en texto plano
     * @return true si se restableció exitosamente, false si el token es inválido
     * @throws IllegalArgumentException si la contraseña no cumple requisitos mínimos
     */
    @Transactional
    public boolean restablecerContrasena(UUID token, String nuevaContrasena) {
        log.info("Intento de restablecimiento de contraseña con token: {}", token);

        // Validar longitud mínima de contraseña
        if (nuevaContrasena == null || nuevaContrasena.length() < 8) {
            log.warn("Contraseña no cumple requisitos mínimos (8 caracteres)");
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }

        // TAREA 7: Validar el token
        if (!validarToken(token)) {
            log.warn("Token inválido o expirado, no se puede restablecer contraseña");
            return false;
        }

        // Buscar el token en la base de datos
        Optional<PasswordResetToken> tokenOpt = passwordResetTokenRepository.findByToken(token);

        if (tokenOpt.isEmpty()) {
            return false;
        }

        PasswordResetToken resetToken = tokenOpt.get();
        Usuario usuario = resetToken.getUsuario();

        // TAREA 8: Hashear la nueva contraseña con BCrypt ANTES de guardar
        String contrasenaHasheada = hashPassword(nuevaContrasena);

        // Actualizar contraseña del usuario
        usuario.setPassword(contrasenaHasheada);
        usuarioRepository.save(usuario);

        // TAREA 9: Marcar el token como usado para prevenir reutilización
        resetToken.setUsedAt(ZonedDateTime.now());
        passwordResetTokenRepository.save(resetToken);

        log.info("Contraseña restablecida exitosamente para usuario ID: {} (token marcado como usado)",
                usuario.getIdUsuario());

        return true;
    }

    /**
     * Limpia tokens expirados de la base de datos
     * Este método puede ser ejecutado periódicamente por un scheduler
     *
     * @return Cantidad de tokens eliminados
     */
    @Transactional
    public int limpiarTokensExpirados() {
        log.info("Limpiando tokens expirados de la base de datos");

        ZonedDateTime ahora = ZonedDateTime.now();
        var tokensExpirados = passwordResetTokenRepository.findExpiredTokens(ahora);

        int cantidad = tokensExpirados.size();
        passwordResetTokenRepository.deleteAll(tokensExpirados);

        log.info("Se eliminaron {} tokens expirados", cantidad);

        return cantidad;
    }
}
