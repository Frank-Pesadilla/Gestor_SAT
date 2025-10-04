package com.gestor.gestor_sat.model.enums;

public enum TipoNotificacion {
    INFO("Información"),
    ADVERTENCIA("Advertencia"),
    ERROR("Error"),
    EXITO("Éxito");
    
    private final String descripcion;
    
    TipoNotificacion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
