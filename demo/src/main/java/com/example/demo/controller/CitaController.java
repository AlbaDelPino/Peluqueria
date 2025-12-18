package com.example.demo.controller;

import com.example.demo.domain.Cita;
import com.example.demo.domain.Cliente;
import com.example.demo.domain.EstadoCita;
import com.example.demo.exception.CitaNotFoundException;
import com.example.demo.security.service.CitaService;
import com.example.demo.payload.request.CitaRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    // 1. Obtener todas las citas
    // 1. Mostrar todas las citas (Solo para ADMIN y GRUPO)
    @GetMapping("/todas")
    @PreAuthorize("hasAnyRole('ADMIN', 'GRUPO')")
    public ResponseEntity<List<Cita>> listarTodas() {
        return ResponseEntity.ok(citaService.findAll());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Cita>> listarCitasCliente(@PathVariable Long clienteId) {
        try {
            List<Cita> citas = citaService.findByCliente(clienteId);
            return ResponseEntity.ok(citas);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // 2. Obtener cita por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cita> getCitaById(@PathVariable long id) {
        return citaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Crear una nueva cita (Lógica de validación de plazas incluida)
    @PostMapping("/reservar")
    public ResponseEntity<?> reservarCita(@RequestBody CitaRequest request) {
        try {
            // Creamos el objeto Cita base
            Cita cita = new Cita();
            cita.setFecha(request.getFecha());
            cita.setHora(request.getHora());

            // Llamamos al service pasando los datos extraídos del JSON
            Cita nuevaCita = citaService.addCita(cita, request.getServicioId(), request.getClienteId());

            return new ResponseEntity<>(nuevaCita, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 4. Cambiar estado de la cita (Confirmar, Cancelar, etc.)
    // Si el estado es CANCELADO, libera la plaza automáticamente
    @PutMapping("/{id}/estado/{nuevoEstado}")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable long id,
            @PathVariable EstadoCita nuevoEstado) {
        try {
            Cita citaActualizada = citaService.cambiarEstado(id, nuevoEstado);
            return ResponseEntity.ok(citaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 5. Eliminar una cita física (También libera la plaza)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCita(@PathVariable long id) {
        try {
            citaService.deleteCita(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 6. Buscar citas por fecha específica
    @GetMapping("/fecha")
    public ResponseEntity<List<Cita>> getCitasByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(citaService.findByFecha(fecha));
    }
}