package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.entity.Bitacora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface BitacoraRepository extends JpaRepository<Bitacora, Long> {

    List<Bitacora> findByUsuarioContainingIgnoreCase(String usuario);

    @Query("SELECT b FROM Bitacora b WHERE b.fecha BETWEEN :inicio AND :fin")
    List<Bitacora> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    List<Bitacora> findByAccionContainingIgnoreCase(String accion);
}
