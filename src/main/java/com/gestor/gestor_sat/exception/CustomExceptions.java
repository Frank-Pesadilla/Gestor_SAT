package com.gestor.gestor_sat.exception;

/**
 * Excepciones personalizadas del sistema
 */
public class CustomExceptions {

    public static class DpiYaExisteException extends RuntimeException {
        public DpiYaExisteException(String dpi) {
            super("Ya existe un cliente registrado con el DPI: " + dpi);
        }
    }

    public static class ClienteNoEncontradoException extends RuntimeException {
        public ClienteNoEncontradoException(Long id) {
            super("No se encontró el cliente con ID: " + id);
        }
        
        public ClienteNoEncontradoException(String dpi) {
            super("No se encontró el cliente con DPI: " + dpi);
        }
    }

    public static class UsuarioNoEncontradoException extends RuntimeException {
        public UsuarioNoEncontradoException(Long id) {
            super("No se encontró el usuario con ID: " + id);
        }
        
        public UsuarioNoEncontradoException(String mensaje) {
            super("No se encontró el usuario: " + mensaje);
        }
    }

    public static class UsuarioYaExisteException extends RuntimeException {
        public UsuarioYaExisteException(String usuario) {
            super("Ya existe un usuario registrado con el nombre: " + usuario);
        }
    }

    public static class EmailYaExisteException extends RuntimeException {
        public EmailYaExisteException(String email) {
            super("Ya existe un usuario registrado con el email: " + email);
        }
    }

    public static class UsuarioYaTieneClienteException extends RuntimeException {
        public UsuarioYaTieneClienteException(Long idUsuario) {
            super("El usuario con ID " + idUsuario + " ya tiene un cliente asociado");
        }
    }

    public static class EdadMinimaException extends RuntimeException {
        public EdadMinimaException() {
            super("El cliente debe ser mayor de 18 años");
        }
    }

    public static class TipoTramiteNoEncontradoException extends RuntimeException {
        public TipoTramiteNoEncontradoException(Long id) {
            super("No se encontró el tipo de trámite con ID: " + id);
        }
        
        public TipoTramiteNoEncontradoException(String mensaje) {
            super("No se encontró el tipo de trámite: " + mensaje);
        }
    }

    public static class TramiteNoEncontradoException extends RuntimeException {
        public TramiteNoEncontradoException(Long id) {
            super("No se encontró el trámite con ID: " + id);
        }
    }

    public static class ArchivoNoEncontradoException extends RuntimeException {
        public ArchivoNoEncontradoException(Long id) {
            super("No se encontró el archivo con ID: " + id);
        }
    }

    public static class ArchivoNoValidoException extends RuntimeException {
        public ArchivoNoValidoException(String mensaje) {
            super(mensaje);
        }
    }

    public static class ArchivoVacioException extends RuntimeException {
        public ArchivoVacioException() {
            super("El archivo está vacío");
        }
    }

    public static class TamanoArchivoExcedidoException extends RuntimeException {
        public TamanoArchivoExcedidoException() {
            super("El archivo excede el tamaño máximo permitido de 10MB");
        }
    }

    public static class TipoArchivoNoPermitidoException extends RuntimeException {
        public TipoArchivoNoPermitidoException(String tipo) {
            super("El tipo de archivo no está permitido: " + tipo + ". Solo se permiten PDF, JPG y PNG");
        }
    }

    public static class ErrorAlmacenamientoArchivoException extends RuntimeException {
        public ErrorAlmacenamientoArchivoException(String mensaje) {
            super("Error al almacenar el archivo: " + mensaje);
        }
    }

    public static class RecursoNoEncontradoException extends RuntimeException {
        public RecursoNoEncontradoException(String mensaje) {
            super(mensaje);
        }

        public RecursoNoEncontradoException(String recurso, Long id) {
            super("No se encontró " + recurso + " con ID: " + id);
        }
    }

    public static class NITInvalidoException extends RuntimeException {
        public NITInvalidoException(String mensaje) {
            super(mensaje);
        }
    }

    // Búsqueda avanzada
    public static class FiltrosVaciosException extends RuntimeException {
        public FiltrosVaciosException(String mensaje) {
            super(mensaje);
        }
    }
}