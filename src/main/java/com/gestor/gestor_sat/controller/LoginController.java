package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.dto.LoginRequestDTO;
import com.gestor.gestor_sat.dto.LoginResponseDTO;
import com.gestor.gestor_sat.entity.Usuario;
import com.gestor.gestor_sat.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth") // ⚠️ Cambia a "/api/auth" si tu backend usa el prefijo /api
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class LoginController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // definido en PasswordEncoderConfig

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        // 1) Buscar por usuario; si no, por email
        Optional<Usuario> userOpt = usuarioRepository.findByUsuario(request.getUsername());
        if (userOpt.isEmpty()) {
            userOpt = usuarioRepository.findByEmail(request.getUsername());
        }
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(null, "Usuario o contraseña incorrectos", null));
        }

        Usuario user = userOpt.get();

        // 2) Validar contraseña
        //    - Si está hasheada con BCrypt (comienza con $2a/$2b/$2y), usar matches
        //    - Si por error está en texto plano, hacer equals como fallback
        String stored = String.valueOf(user.getPassword());
        boolean isBcrypt = stored != null && stored.startsWith("$2");
        boolean ok = isBcrypt
                ? passwordEncoder.matches(request.getPassword(), stored)
                : request.getPassword().equals(stored);

        if (!ok) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(null, "Usuario o contraseña incorrectos", null));
        }

        // 3) Token simulado (reemplaza por JWT cuando quieras)
        String token = "token-" + (user.getUsuario() != null ? user.getUsuario() : user.getEmail())
                + "-" + System.currentTimeMillis();

        return ResponseEntity.ok(new LoginResponseDTO(
                (user.getUsuario() != null ? user.getUsuario() : user.getEmail()),
                "Login exitoso",
                token
        ));
    }
}
