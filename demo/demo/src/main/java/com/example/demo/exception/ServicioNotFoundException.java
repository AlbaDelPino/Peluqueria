package com.example.demo.exception;


import java.util.Optional;
import java.util.Set;

public class ServicioNotFoundException extends RuntimeException {
    public ServicioNotFoundException() {
        super();
    }

    public ServicioNotFoundException(String message) {
        super(message);
    }

    public ServicioNotFoundException(long id) {
        super("Alumno not found: " + id);
    }


}
