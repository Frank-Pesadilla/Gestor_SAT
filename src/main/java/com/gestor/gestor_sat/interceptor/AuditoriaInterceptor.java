package com.gestor.gestor_sat.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.gestor.gestor_sat.service.BitacoraService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditoriaInterceptor implements HandlerInterceptor {
    
    private final BitacoraService bitacoraService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
                            Object handler) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        
        // Solo registrar operaciones de modificación
        if (method.equals("POST") || method.equals("PUT") || method.equals("DELETE")) {
            String accion = method + " " + uri;
            String ipAddress = obtenerIPCliente(request);
            String userAgent = request.getHeader("User-Agent");
            
            // Obtener usuario del contexto (implementar cuando tengan Spring Security)
            Long idUsuario = null;
            
            bitacoraService.registrarAccion(accion, "Petición HTTP", idUsuario, 
                                          ipAddress, userAgent);
        }
        
        return true;
    }
    
    private String obtenerIPCliente(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

