package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para gestión de notificaciones
 * CU-SAT004: Generar Notificaciones
 */
@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    /**
     * Encuentra todas las notificaciones NO EXPIRADAS de un usuario ordenadas por fecha descendente
     */
    List<Notificacion> findByUsuarioIdUsuarioAndExpiradaFalseOrderByFechaDesc(Long idUsuario);

    /**
     * Encuentra notificaciones NO EXPIRADAS y no leídas de un usuario
     */
    List<Notificacion> findByUsuarioIdUsuarioAndLeidaFalseAndExpiradaFalseOrderByFechaDesc(Long idUsuario);

    /**
     * Cuenta notificaciones NO EXPIRADAS y no leídas de un usuario
     */
    Long countByUsuarioIdUsuarioAndLeidaFalseAndExpiradaFalse(Long idUsuario);

    /**
     * Encuentra notificaciones que han excedido su fecha de expiración
     */
    List<Notificacion> findByFechaExpiracionBeforeAndExpiradaFalse(LocalDateTime fecha);

    /**
     * Marca notificaciones como expiradas basándose en la fecha de expiración
     */
    @Modifying
    @Query("UPDATE Notificacion n SET n.expirada = true WHERE n.fechaExpiracion < :fecha AND n.expirada = false")
    int marcarComoExpiradas(@Param("fecha") LocalDateTime fecha);

    /**
     * Elimina físicamente las notificaciones expiradas después de cierto tiempo
     */
    @Modifying
    @Query("DELETE FROM Notificacion n WHERE n.expirada = true AND n.fechaExpiracion < :fecha")
    int eliminarNotificacionesExpiradas(@Param("fecha") LocalDateTime fecha);

    /**
     * Encuentra notificaciones próximas a expirar (dentro de X días)
     */
    @Query("SELECT n FROM Notificacion n WHERE n.usuario.idUsuario = :idUsuario " +
           "AND n.expirada = false " +
           "AND n.fechaExpiracion BETWEEN :ahora AND :fechaLimite " +
           "ORDER BY n.fechaExpiracion ASC")
    List<Notificacion> findProximasAExpirar(
        @Param("idUsuario") Long idUsuario,
        @Param("ahora") LocalDateTime ahora,
        @Param("fechaLimite") LocalDateTime fechaLimite
    );

    /**
     * Cuenta notificaciones totales (incluidas expiradas) - para estadísticas
     */
    Long countByUsuarioIdUsuario(Long idUsuario);
}
