package com.example.demo.exception;

public class HorarioNotFoundException extends RuntimeException {
    public HorarioNotFoundException() {
        super();
    }

    public HorarioNotFoundException(String message) {
        super(message);
    }

    public HorarioNotFoundException(long id) {
        super("Horario not found: " + id);
    }
}
