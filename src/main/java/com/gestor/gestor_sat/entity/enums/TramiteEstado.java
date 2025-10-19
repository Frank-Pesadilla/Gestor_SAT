package com.gestor.gestor_sat.entity.enums;

/**
 * Estados posibles para un trámite
 */
public enum TramiteEstado {
    INICIADO("Trámite recién iniciado"),
    PENDIENTE("Trámite en proceso de revisión"),
    TERMINADO("Trámite completado");

    private final String descripcion;

    TramiteEstado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}