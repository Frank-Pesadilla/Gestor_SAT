package com.gestor.gestor_sat.scheduler;

import com.gestor.gestor_sat.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Scheduler para limpieza automática de notificaciones expiradas
 * Se ejecuta periódicamente según la configuración en application.properties
 */
@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(
    value = "app.notificaciones.limpieza-automatica.enabled",
    havingValue = "true",
    matchIfMissing = true
)
public class NotificacionLimpiezaScheduler {

    private final NotificacionRepository notificacionRepository;

    @Value("${app.notificaciones.dias-retencion-post-expiracion:7}")
    private int diasRetencionPostExpiracion;

    /**
     * PASO 1: Marca como expiradas las notificaciones que ya pasaron su fecha de expiración
     * Se ejecuta según el cron configurado (default: diario a las 2:00 AM)
     */
    @Scheduled(cron = "${app.notificaciones.limpieza-automatica.cron:0 0 2 * * ?}")
    @Transactional
    public void marcarNotificacionesExpiradas() {
        log.info("=== Iniciando proceso de marcado de notificaciones expiradas ===");

        try {
            LocalDateTime ahora = LocalDateTime.now();
            int notificacionesMarcadas = notificacionRepository.marcarComoExpiradas(ahora);

            if (notificacionesMarcadas > 0) {
                log.info("✓ Se marcaron {} notificaciones como expiradas", notificacionesMarcadas);
            } else {
                log.info("✓ No hay notificaciones para marcar como expiradas");
            }

        } catch (Exception e) {
            log.error("✗ Error al marcar notificaciones como expiradas: {}", e.getMessage(), e);
        }
    }

    /**
     * PASO 2: Elimina físicamente las notificaciones que llevan tiempo expiradas
     * Se ejecuta según el cron configurado (default: diario a las 2:00 AM)
     *
     * Flujo: Notificación creada → Expira después de X días → Se marca como expirada
     *        → Se mantiene Y días más → Se elimina físicamente
     */
    @Scheduled(cron = "${app.notificaciones.limpieza-automatica.cron:0 0 2 * * ?}")
    @Transactional
    public void eliminarNotificacionesExpiradas() {
        log.info("=== Iniciando proceso de eliminación física de notificaciones expiradas ===");

        try {
            // Eliminar notificaciones que expiraron hace X días
            LocalDateTime fechaLimite = LocalDateTime.now().minusDays(diasRetencionPostExpiracion);
            int notificacionesEliminadas = notificacionRepository.eliminarNotificacionesExpiradas(fechaLimite);

            if (notificacionesEliminadas > 0) {
                log.info("✓ Se eliminaron {} notificaciones expiradas de la base de datos", notificacionesEliminadas);
            } else {
                log.info("✓ No hay notificaciones expiradas para eliminar");
            }

        } catch (Exception e) {
            log.error("✗ Error al eliminar notificaciones expiradas: {}", e.getMessage(), e);
        }

        log.info("=== Proceso de limpieza de notificaciones finalizado ===");
    }

    /**
     * Método manual para ejecutar la limpieza completa (marcar y eliminar)
     * Útil para pruebas o ejecución manual desde un endpoint administrativo
     */
    @Transactional
    public void ejecutarLimpiezaCompleta() {
        log.info("=== Ejecutando limpieza completa manual de notificaciones ===");
        marcarNotificacionesExpiradas();
        eliminarNotificacionesExpiradas();
    }
}
