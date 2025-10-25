package com.gestor.gestor_sat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;

/**
 * Configuración de CORS (Cross-Origin Resource Sharing)
 * Permite que el frontend pueda hacer peticiones al backend desde diferentes orígenes
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                    "http://localhost:5500",        // Live Server (VS Code)
                    "http://localhost:3000",        // React/Vue dev server (por si acaso)
                    "http://127.0.0.1:5500",        // Alternativa de localhost
                    "http://localhost:8000"         // Python HTTP server
                    // Agrega aquí la URL de producción cuando despliegues:
                    // "https://tu-dominio.com"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * Configuración alternativa con Bean de CorsFilter
     * Proporciona mayor control sobre la configuración CORS
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Collections.singletonList("*")); // En producción, especifica los dominios exactos
        config.setAllowedHeaders(Arrays.asList(
            "Origin",
            "Content-Type",
            "Accept",
            "Authorization",
            "X-Requested-With",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        config.setExposedHeaders(Arrays.asList(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials",
            "Authorization",
            "Content-Disposition"
        ));
        config.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        config.setMaxAge(3600L);
        
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}