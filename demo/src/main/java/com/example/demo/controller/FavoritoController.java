package com.example.demo.controller;

import com.example.demo.domain.Servicio;

import com.example.demo.security.service.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/favoritos")
@CrossOrigin(origins = "*")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    // --- GET: Obtener todos los favoritos de un cliente ---
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Set<Servicio>> listarFavoritos(@PathVariable Long clienteId) {
        Set<Servicio> favoritos = favoritoService.listarFavoritos(clienteId);
        return ResponseEntity.ok(favoritos);
    }

    // --- POST: Añadir un servicio a favoritos ---
    // Se usa el ID del cliente (heredado de User) y el id_servicio
    @PostMapping("/cliente/{clienteId}/servicio/{servicioId}")
    public ResponseEntity<String> agregarFavorito(
            @PathVariable Long clienteId,
            @PathVariable Long servicioId) {
        try {
            favoritoService.agregarAFavoritos(clienteId, servicioId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Servicio añadido a favoritos.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al añadir: " + e.getMessage());
        }
    }

    // --- DELETE: Quitar un servicio de favoritos ---
    @DeleteMapping("/cliente/{clienteId}/servicio/{servicioId}")
    public ResponseEntity<String> eliminarFavorito(
            @PathVariable Long clienteId,
            @PathVariable Long servicioId) {
        try {
            favoritoService.eliminarDeFavoritos(clienteId, servicioId);
            return ResponseEntity.ok("Servicio eliminado de favoritos.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al eliminar: " + e.getMessage());
        }
    }
}