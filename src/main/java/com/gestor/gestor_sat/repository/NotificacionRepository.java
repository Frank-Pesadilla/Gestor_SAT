package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.model.Notificacion;
import com.gestor.gestor_sat.model.Usuario;
import com.gestor.gestor_sat.model.enums.TipoNotificacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    
    Page<Notificacion> findByUsuarioOrderByCreatedAtDesc(Usuario usuario, Pageable pageable);
    
    List<Notificacion> findByUsuarioAndLeidaFalseOrderByCreatedAtDesc(Usuario usuario);
    
    Long countByUsuarioAndLeidaFalse(Usuario usuario);
    
    Page<Notificacion> findByUsuarioAndTipoOrderByCreatedAtDesc(
            Usuario usuario, TipoNotificacion tipo, Pageable pageable);
}
