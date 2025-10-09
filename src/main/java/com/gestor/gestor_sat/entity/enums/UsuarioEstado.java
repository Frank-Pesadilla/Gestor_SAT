package com.gestor.gestor_sat.entity.enums;

/**
 * Estados posibles para un usuario del sistema
 */
public enum UsuarioEstado {
    ACTIVO("Usuario activo en el sistema"),
    BLOQUEADO("Usuario bloqueado, no puede acceder");

    private final String descripcion;

    UsuarioEstado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}