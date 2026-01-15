package com.example.demo.controller;

import com.example.demo.domain.Cita;
import com.example.demo.domain.EstadoCita;
import com.example.demo.security.service.CitaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    // 1. Mostrar todas las citas (Solo para ADMIN y GRUPO)
    @GetMapping("/todas")
    @PreAuthorize("hasAnyRole('ADMIN', 'GRUPO')")
    public ResponseEntity<List<Cita>> listarTodas() {
        return ResponseEntity.ok(citaService.findAll());
    }

    // 2. Citas de un cliente
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Cita>> listarCitasCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(citaService.findByCliente(clienteId));
    }

    // 3. Obtener cita por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cita> getCitaById(@PathVariable long id) {
        return citaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/disponible")
    public ResponseEntity<?> getCitasDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,@RequestParam Long horarioId) {
        return ResponseEntity.ok(citaService.citasDisponibles(horarioId,fecha));
    }

    @GetMapping("/today")
    public ResponseEntity<List<Cita>> getCitasDeHoy() {
        LocalDate hoy = LocalDate.now();
        return ResponseEntity.ok(citaService.findByFecha(hoy));
    }





    // 4. Crear una nueva cita
    @PostMapping("/reservar")
    public ResponseEntity<Cita> addCita(@RequestBody Cita cita) {
        Cita added = citaService.addCita(cita);
        return new ResponseEntity<>(added, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/estado/{nuevoEstado}")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable long id,
            @PathVariable boolean nuevoEstado) {

        try {
            Cita citaActualizada = citaService.cambiarEstado(id, nuevoEstado);
            return ResponseEntity.ok(citaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }





    // 6. Eliminar una cita
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCita(@PathVariable long id) {
        try {
            citaService.deleteCita(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 7. Buscar citas por fecha
    @GetMapping("/fecha")
    public ResponseEntity<List<Cita>> getCitasByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(citaService.findByFecha(fecha));
    }
}
