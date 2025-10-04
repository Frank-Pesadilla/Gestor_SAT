package com.gestor.gestor_sat.service.security; 
 
<<<<<<< HEAD
<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.stereotype.Service; 
import javax.crypto.Cipher; 
import javax.crypto.spec.SecretKeySpec; 
import java.util.Base64; 
=======
=======
>>>>>>> Mafer
import java.util.Base64; 

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service; 
<<<<<<< HEAD
>>>>>>> Mafer
=======
>>>>>>> Mafer
 
@Service 
public class AESEncryptionService { 
     
    @Value("${aes.secret.key}") 
    private String secretKey; 
     
    private static final String ALGORITHM = "AES"; 
     
    public String encrypt(String data) { 
        try { 
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM); 
            Cipher cipher = Cipher.getInstance(ALGORITHM); 
            cipher.init(Cipher.ENCRYPT_MODE, keySpec); 
            byte[] encrypted = cipher.doFinal(data.getBytes()); 
            return Base64.getEncoder().encodeToString(encrypted); 
        } catch (Exception e) { 
            throw new RuntimeException("Error al encriptar: " + e.getMessage(), e); 
        } 
    } 
     
    public String decrypt(String encryptedData) { 
        try { 
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM); 
            Cipher cipher = Cipher.getInstance(ALGORITHM); 
            cipher.init(Cipher.DECRYPT_MODE, keySpec); 
            byte[] decoded = Base64.getDecoder().decode(encryptedData); 
            byte[] decrypted = cipher.doFinal(decoded); 
            return new String(decrypted); 
        } catch (Exception e) { 
            throw new RuntimeException("Error al desencriptar: " + e.getMessage(), e); 
        } 
    } 
<<<<<<< HEAD
<<<<<<< HEAD
}
=======
} 
>>>>>>> Mafer
=======
} 
>>>>>>> Mafer
