package com.gestor.gestor_sat.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Servicio para encriptación/desencriptación AES-256
 * Usado para proteger contraseñas sensibles en la base de datos
 */
@Service
@Slf4j
public class AESEncryptionService {

    private static final String ALGORITHM = "AES";
    
    @Value("${app.security.aes.secret-key}")
    private String secretKey;

    /**
     * Encripta un texto usando AES-256
     * @param plainText texto a encriptar
     * @return texto encriptado en Base64
     */
    public String encrypt(String plainText) {
        try {
            if (plainText == null || plainText.isEmpty()) {
                return null;
            }

            SecretKeySpec keySpec = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8), 
                ALGORITHM
            );
            
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
            
        } catch (Exception e) {
            log.error("Error al encriptar: {}", e.getMessage());
            throw new RuntimeException("Error en la encriptación", e);
        }
    }

    /**
     * Desencripta un texto usando AES-256
     * @param encryptedText texto encriptado en Base64
     * @return texto desencriptado
     */
    public String decrypt(String encryptedText) {
        try {
            if (encryptedText == null || encryptedText.isEmpty()) {
                return null;
            }

            SecretKeySpec keySpec = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8), 
                ALGORITHM
            );
            
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decrypted, StandardCharsets.UTF_8);
            
        } catch (Exception e) {
            log.error("Error al desencriptar: {}", e.getMessage());
            throw new RuntimeException("Error en la desencriptación", e);
        }
    }
}