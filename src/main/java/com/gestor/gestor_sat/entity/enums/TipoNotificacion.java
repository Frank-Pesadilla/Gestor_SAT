package com.gestor.gestor_sat.entity.enums;

/**
 * Tipos de notificación del sistema
 * CU-SAT004: Generar Notificaciones
 */
public enum TipoNotificacion {
    INFO("Información general"),
    ADVERTENCIA("Advertencia o alerta"),
    ERROR("Error o problema"),
    EXITO("Operación exitosa");

    private final String descripcion;

    TipoNotificacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
