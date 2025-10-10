package com.gestor.gestor_sat.repository;

import com.gestor.gestor_sat.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;


@Repository
// Cambia la firma para extender JpaSpecificationExecutor
public interface ClienteRepository extends JpaRepository<Cliente, Long>, org.springframework.data.jpa.repository.JpaSpecificationExecutor<Cliente> {
    Optional<Cliente> findByDpi(String dpi);
    boolean existsByDpi(String dpi);
    boolean existsByUsuarioId(Long idUsuario);

    Optional<Cliente> findByUsuarioIdUsuario(Long idUsuario);

    /**
     * CU-SAT012: Panel de Control - Tarea 7
     * Cuenta clientes nuevos registrados en un rango de fechas
     * Usado para calcular clientes del mes actual
     *
     * @param inicio Fecha y hora de inicio del rango
     * @param fin Fecha y hora de fin del rango
     * @return Cantidad de clientes registrados en el rango
     */
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.createdAt BETWEEN :inicio AND :fin")
    long countByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * CU-SAT012: Panel de Control - Tarea 9
     * Cuenta clientes nuevos por mes y año específico
     * Usado para gráficas de clientes por mes
     *
     * @param mes Número del mes (1-12)
     * @param anio Año
     * @return Cantidad de clientes registrados en ese mes
     */
    @Query("SELECT COUNT(c) FROM Cliente c WHERE MONTH(c.createdAt) = :mes AND YEAR(c.createdAt) = :anio")
    long countByMesAndAnio(int mes, int anio);
}
