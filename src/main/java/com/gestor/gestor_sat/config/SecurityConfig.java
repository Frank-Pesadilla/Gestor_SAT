package com.gestor.gestor_sat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuración de Spring Security
 * Configuración básica para desarrollo - SIN autenticación
 * Los compañeros pueden agregar JWT u otro método de autenticación después
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para APIs REST
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Habilitar CORS
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Permitir todas las peticiones sin autenticación
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sin sesiones
            );

        return http.build();
    }

    /**
     * Configuración de CORS
     * Permite peticiones desde cualquier origen (desarrollo)
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Permitir todos los orígenes
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(false);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}

/*
 * NOTA PARA EL EQUIPO:
 * Esta es una configuración básica SIN autenticación para facilitar el desarrollo.
 * 
 * Para producción, se debe implementar:
 * 1. Autenticación JWT
 * 2. Roles y permisos
 * 3. CORS restrictivo
 * 4. HTTPS obligatorio
 * 
 * Ejemplo de configuración con autenticación:
 * 
 * .authorizeHttpRequests(auth -> auth
 *     .requestMatchers("/api/auth/**").permitAll()
 *     .requestMatchers("/api/admin/**").hasRole("ADMIN")
 *     .anyRequest().authenticated()
 * )
 */