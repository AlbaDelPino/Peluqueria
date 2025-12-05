package com.example.demo.exception;

public class CitaNotFoundException extends RuntimeException {

    public CitaNotFoundException() {
        super();
    }

    public CitaNotFoundException(String message) {
        super(message);
    }

    public CitaNotFoundException(long id) {
        super("Cita not found: " + id);
    }
}
