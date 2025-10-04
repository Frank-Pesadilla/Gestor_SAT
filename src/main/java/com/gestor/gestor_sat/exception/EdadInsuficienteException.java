package com.gestor.gestor_sat.exception;

public class EdadInsuficienteException extends RuntimeException {
    public EdadInsuficienteException(Integer edad) {
        super("El cliente debe ser mayor de 18 años. Edad actual: " + edad + " años");
    }
}