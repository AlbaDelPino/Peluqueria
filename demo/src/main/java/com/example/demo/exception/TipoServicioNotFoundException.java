package com.example.demo.exception;

public class TipoServicioNotFoundException extends RuntimeException {
    public TipoServicioNotFoundException() {
        super();
    }

    public TipoServicioNotFoundException(String message) {
        super(message);
    }

    public TipoServicioNotFoundException(long id) {
        super("Tipo de servicio not found: " + id);
    }
}