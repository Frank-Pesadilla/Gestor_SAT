package com.gestor.gestor_sat.audit;

import com.gestor.gestor_sat.service.BitacoraService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuditoriaInterceptor implements HandlerInterceptor {

    private final BitacoraService bitacoraService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String usuario = SecurityContextHolder.getContext().getAuthentication() != null ?
                SecurityContextHolder.getContext().getAuthentication().getName() : "ANONIMO";
        String accion = request.getMethod() + " " + request.getRequestURI();
        String detalles = "status=" + response.getStatus();
        String ip = obtenerIp(request);
        bitacoraService.registrarAccion(usuario, accion, detalles, ip); // 4.7
    }

    private String obtenerIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) ip = request.getRemoteAddr();
        return ip;
    }
}
