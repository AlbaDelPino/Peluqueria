package com.example.demo.exception;

public class CursoNotFoundException extends RuntimeException {
    public CursoNotFoundException() {
        super();
    }

    public CursoNotFoundException(String message) {
        super(message);
    }

    public CursoNotFoundException(long id) {
        super("Curso not found: " + id);
    }
}
