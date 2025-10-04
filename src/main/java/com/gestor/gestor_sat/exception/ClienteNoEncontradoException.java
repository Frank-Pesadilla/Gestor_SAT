package com.gestor.gestor_sat.exception;

public class ClienteNoEncontradoException extends RuntimeException {
    public ClienteNoEncontradoException(Long id) {
        super("No se encontró el cliente con ID: " + id);
    }
    
    public ClienteNoEncontradoException(String dpi) {
        super("No se encontró el cliente con DPI: " + dpi);
    }
}