package com.example.demo.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // --- 1. CAMBIA ESTA CADENA POR LA CONTRASEÑA QUE QUIERES ENCRIPTAR ---
        String rawPassword = "admin18";

        // 2. Genera el hash
        String hashedPassword = encoder.encode(rawPassword);

        // 3. Imprime el hash
        System.out.println("El Hash BCrypt es: " + hashedPassword);
    }
}