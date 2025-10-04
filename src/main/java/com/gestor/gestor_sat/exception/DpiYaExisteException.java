package com.gestor.gestor_sat.exception;

public class DpiYaExisteException extends RuntimeException {
    public DpiYaExisteException(String dpi) {
        super("Ya existe un cliente registrado con el DPI: " + dpi);
    }
}