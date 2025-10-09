package com.gestor.gestor_sat.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomExceptions.DpiYaExisteException.class)
    public ResponseEntity<ErrorResponse> handleDpiYaExiste(CustomExceptions.DpiYaExisteException ex) {
        log.error("DPI ya existe: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomExceptions.ClienteNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleClienteNoEncontrado(CustomExceptions.ClienteNoEncontradoException ex) {
        log.error("Cliente no encontrado: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomExceptions.UsuarioNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioNoEncontrado(CustomExceptions.UsuarioNoEncontradoException ex) {
        log.error("Usuario no encontrado: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomExceptions.UsuarioYaExisteException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioYaExiste(CustomExceptions.UsuarioYaExisteException ex) {
        log.error("Usuario ya existe: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomExceptions.EmailYaExisteException.class)
    public ResponseEntity<ErrorResponse> handleEmailYaExiste(CustomExceptions.EmailYaExisteException ex) {
        log.error("Email ya existe: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomExceptions.UsuarioYaTieneClienteException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioYaTieneCliente(CustomExceptions.UsuarioYaTieneClienteException ex) {
        log.error("Usuario ya tiene cliente: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomExceptions.EdadMinimaException.class)
    public ResponseEntity<ErrorResponse> handleEdadMinima(CustomExceptions.EdadMinimaException ex) {
        log.error("Edad mínima no cumplida: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomExceptions.TipoTramiteNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleTipoTramiteNoEncontrado(CustomExceptions.TipoTramiteNoEncontradoException ex) {
        log.error("Tipo trámite no encontrado: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomExceptions.TramiteNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleTramiteNoEncontrado(CustomExceptions.TramiteNoEncontradoException ex) {
        log.error("Trámite no encontrado: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
        CustomExceptions.ArchivoVacioException.class,
        CustomExceptions.TamanoArchivoExcedidoException.class,
        CustomExceptions.TipoArchivoNoPermitidoException.class,
        CustomExceptions.ArchivoNoValidoException.class
    })
    public ResponseEntity<ErrorResponse> handleArchivoValidacion(RuntimeException ex) {
        log.error("Error de validación de archivo: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomExceptions.ArchivoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleArchivoNoEncontrado(CustomExceptions.ArchivoNoEncontradoException ex) {
        log.error("Archivo no encontrado: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomExceptions.ErrorAlmacenamientoArchivoException.class)
    public ResponseEntity<ErrorResponse> handleErrorAlmacenamiento(CustomExceptions.ErrorAlmacenamientoArchivoException ex) {
        log.error("Error al almacenar archivo: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("Errores de validación: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Error inesperado: ", ex);
        return buildErrorResponse("Error interno del servidor: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String mensaje, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            mensaje
        );
        return new ResponseEntity<>(errorResponse, status);
    }

    // Clase interna para la respuesta de error
    public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String mensaje
    ) {}
}