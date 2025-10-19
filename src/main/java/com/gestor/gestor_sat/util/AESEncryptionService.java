package com.gestor.gestor_sat.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Servicio para encriptación/desencriptación AES-256 con CBC e IV aleatorio
 * CU-SAT006: Gestión de Contraseñas por Plataforma
 *
 * MEJORAS DE SEGURIDAD:
 * - Usa AES/CBC/PKCS5Padding en lugar de ECB (más seguro)
 * - Genera IV (Initialization Vector) aleatorio para cada encriptación
 * - Almacena IV junto con el texto cifrado
 * - Cumple con estándares de seguridad modernos
 *
 * Usado para proteger contraseñas sensibles en la base de datos
 */
@Service
@Slf4j
public class AESEncryptionService {

    // AES/CBC/PKCS5Padding es más seguro que AES (ECB) por defecto
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_ALGORITHM = "AES";
    private static final int IV_LENGTH = 16; // 128 bits para AES

    @Value("${app.security.aes.secret-key}")
    private String secretKey;

    /**
     * CU-SAT006 - Tarea 1: Encripta un texto usando AES-256 con CBC e IV aleatorio
     *
     * PROCESO DE ENCRIPTACIÓN:
     * 1. Genera un IV (Initialization Vector) aleatorio de 16 bytes
     * 2. Encripta el texto usando AES-256-CBC con la clave secreta y el IV
     * 3. Concatena IV + texto cifrado
     * 4. Codifica todo en Base64 para almacenamiento
     *
     * SEGURIDAD:
     * - Cada encriptación usa un IV diferente (más seguro)
     * - El IV se almacena junto con el texto cifrado (es público, no secreto)
     * - Usa CBC en lugar de ECB (previene patrones repetitivos)
     *
     * @param plainText texto a encriptar (contraseña en texto plano)
     * @return texto encriptado en Base64 (formato: IV + texto cifrado)
     * @throws RuntimeException si ocurre un error en la encriptación
     */
    public String encrypt(String plainText) {
        try {
            // Validar entrada
            if (plainText == null || plainText.isEmpty()) {
                return null;
            }

            // NUNCA loguear el texto plano (contraseña)
            log.debug("Iniciando encriptación de texto");

            // 1. Crear clave secreta AES-256
            SecretKeySpec keySpec = new SecretKeySpec(
                    secretKey.getBytes(StandardCharsets.UTF_8),
                    KEY_ALGORITHM
            );

            // 2. Generar IV aleatorio de 16 bytes (128 bits)
            // IMPORTANTE: Cada encriptación debe tener un IV DIFERENTE
            byte[] iv = new byte[IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // 3. Configurar cipher con AES/CBC/PKCS5Padding
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            // 4. Encriptar el texto
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            // 5. Concatenar IV + texto cifrado
            // Formato: [IV de 16 bytes][texto cifrado de N bytes]
            byte[] ivAndEncrypted = new byte[IV_LENGTH + encrypted.length];
            System.arraycopy(iv, 0, ivAndEncrypted, 0, IV_LENGTH);
            System.arraycopy(encrypted, 0, ivAndEncrypted, IV_LENGTH, encrypted.length);

            // 6. Codificar en Base64 para almacenamiento
            String result = Base64.getEncoder().encodeToString(ivAndEncrypted);

            log.debug("Encriptación completada exitosamente");
            return result;

        } catch (Exception e) {
            // NUNCA loguear el texto plano en el error
            log.error("Error al encriptar texto: {}", e.getMessage());
            throw new RuntimeException("Error en la encriptación", e);
        }
    }

    /**
     * CU-SAT006 - Tarea 1: Desencripta un texto usando AES-256 con CBC
     *
     * PROCESO DE DESENCRIPTACIÓN:
     * 1. Decodifica el texto Base64
     * 2. Extrae el IV de los primeros 16 bytes
     * 3. Extrae el texto cifrado del resto
     * 4. Desencripta usando AES-256-CBC con la clave secreta y el IV
     *
     * SEGURIDAD:
     * - El IV se extrae del texto cifrado (primeros 16 bytes)
     * - Usa el mismo modo CBC que se usó para encriptar
     * - Nunca loguea contraseñas desencriptadas
     *
     * @param encryptedText texto encriptado en Base64 (formato: IV + texto cifrado)
     * @return texto desencriptado (contraseña en texto plano)
     * @throws RuntimeException si ocurre un error en la desencriptación
     */
    public String decrypt(String encryptedText) {
        try {
            // Validar entrada
            if (encryptedText == null || encryptedText.isEmpty()) {
                return null;
            }

            log.debug("Iniciando desencriptación de texto");

            // 1. Decodificar de Base64
            byte[] ivAndEncrypted = Base64.getDecoder().decode(encryptedText);

            // 2. Validar longitud mínima (debe tener al menos el IV)
            if (ivAndEncrypted.length < IV_LENGTH) {
                log.error("Texto encriptado inválido: longitud menor a IV_LENGTH");
                throw new RuntimeException("Texto encriptado inválido");
            }

            // 3. Extraer IV (primeros 16 bytes)
            byte[] iv = new byte[IV_LENGTH];
            System.arraycopy(ivAndEncrypted, 0, iv, 0, IV_LENGTH);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // 4. Extraer texto cifrado (resto de bytes después del IV)
            byte[] encrypted = new byte[ivAndEncrypted.length - IV_LENGTH];
            System.arraycopy(ivAndEncrypted, IV_LENGTH, encrypted, 0, encrypted.length);

            // 5. Crear clave secreta AES-256
            SecretKeySpec keySpec = new SecretKeySpec(
                    secretKey.getBytes(StandardCharsets.UTF_8),
                    KEY_ALGORITHM
            );

            // 6. Configurar cipher con AES/CBC/PKCS5Padding
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            // 7. Desencriptar
            byte[] decrypted = cipher.doFinal(encrypted);
            String result = new String(decrypted, StandardCharsets.UTF_8);

            log.debug("Desencriptación completada exitosamente");
            // NUNCA loguear el resultado (contraseña en texto plano)

            return result;

        } catch (Exception e) {
            // NUNCA loguear el texto desencriptado en el error
            log.error("Error al desencriptar texto: {}", e.getMessage());
            throw new RuntimeException("Error en la desencriptación", e);
        }
    }
}