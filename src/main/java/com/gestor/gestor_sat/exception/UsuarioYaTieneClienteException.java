package com.gestor.gestor_sat.exception;

public class UsuarioYaTieneClienteException extends RuntimeException {
    public UsuarioYaTieneClienteException(Long idUsuario) {
        super("El usuario con ID " + idUsuario + " ya tiene un cliente asociado");
    }
}