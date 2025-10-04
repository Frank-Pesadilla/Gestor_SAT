package com.gestor.gestor_sat.exception;

public class TipoTramiteNoEncontradoException extends RuntimeException {
    public TipoTramiteNoEncontradoException(Long id) {
        super("No se encontró el tipo de trámite con ID: " + id);
    }
}