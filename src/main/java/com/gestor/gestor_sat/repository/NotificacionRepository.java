package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para gestión de notificaciones
 * CU-SAT004: Generar Notificaciones
 */
@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    /**
     * Encuentra todas las notificaciones de un usuario ordenadas por fecha descendente
     */
    List<Notificacion> findByUsuarioIdUsuarioOrderByFechaDesc(Long idUsuario);

    /**
     * Encuentra notificaciones no leídas de un usuario
     */
    List<Notificacion> findByUsuarioIdUsuarioAndLeidaFalseOrderByFechaDesc(Long idUsuario);

    /**
     * Cuenta notificaciones no leídas de un usuario
     */
    Long countByUsuarioIdUsuarioAndLeidaFalse(Long idUsuario);
}
