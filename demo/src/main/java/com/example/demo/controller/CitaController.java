package com.example.demo.controller;

import com.example.demo.domain.Cita;
import com.example.demo.domain.EstadoCita;
import com.example.demo.security.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    @Autowired
    private CitaService citaService;

    // ⭐ Crear cita
    // Cambia 'public Cita' por 'public ResponseEntity<?>'
    @PostMapping("/reservar")
    public ResponseEntity<?> addCita(@RequestBody Cita cita) {
        try {
            Cita nuevaCita = citaService.addCita(cita);
            // Devolvemos la cita envuelta en un 200 OK
            return ResponseEntity.ok(nuevaCita);
        } catch (RuntimeException e) {
            // Devolvemos el error envuelto en un 400 Bad Request
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ⭐ Eliminar cita (solo si está CANCELADA o COMPLETADA)
    @DeleteMapping("/{id}")
    public void deleteCita(@PathVariable long id) {
        citaService.deleteCita(id);
    }

    // ⭐ Cambiar estado (CANCELADO o COMPLETADO)
    @PutMapping("/{id}/estado")
    public Cita cambiarEstado(
            @PathVariable long id,
            @RequestParam EstadoCita estado
    ) {
        return citaService.cambiarEstado(id, estado);
    }

    @PutMapping("/{id}/ficha")
    public Cita completarFicha(
            @PathVariable long id,
            @RequestBody Cita cita
    ) {
        return citaService.cambiarFicha(id, cita);
    }

    // ⭐ Citas disponibles por horario y fecha
    @GetMapping("/disponible")
    public Map<String, Integer> horasConPlazasDisponibles(
            @RequestParam Long horarioId,
            @RequestParam String fecha
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaFormato = LocalDate.parse(fecha, formatter);
        return citaService.horasDisponibles(horarioId, fechaFormato);
    }

    // ⭐ Citas disponibles por horario y fecha
    @GetMapping("/rango")
    public List<Cita> citasDeRango(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaInicioFormato = LocalDate.parse(fechaInicio, formatter);
        LocalDate fechaFinFormato = LocalDate.parse(fechaFin, formatter);
        return citaService.citasDeRango(fechaInicioFormato, fechaFinFormato);
    }


    // ⭐ Horas disponibles por bloques
    @GetMapping("/horas")
    public Map<String, Integer> horasDisponibles(
            @RequestParam Long horarioId,
            @RequestParam String fecha
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaFormato = LocalDate.parse(fecha, formatter);
        return citaService.horasDisponibles(horarioId, fechaFormato);
    }

    // ⭐ Buscar por fecha
    @GetMapping("/fecha")
    public List<Cita> findByFecha(@RequestParam String fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaFormato = LocalDate.parse(fecha, formatter);
        return citaService.findByFecha(fechaFormato);
    }

    // ⭐ Buscar por estado
    @GetMapping("/estado")
    public List<Cita> findByEstado(@RequestParam EstadoCita estado) {
        return citaService.findByEstado(estado);
    }

    // ⭐ Citas de hoy
    @GetMapping("/hoy")
    public List<Cita> citasDeHoy() {
        return citaService.findCitasDeHoy();
    }
    @GetMapping("/todas")
    public List<Cita> getTodasLasCitas() {
        return citaService.findAll();
    }

    @GetMapping("/{id}")
    public Cita getCitaPorId(@PathVariable long id) {
        return citaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }


    @GetMapping("/cliente/{clienteId}")
    public List<Cita> getCitasPorCliente(@PathVariable Long clienteId) {
        return citaService.findByCliente(clienteId);
    }


    // ⭐ Obtener días disponibles por ID de servicio
    @GetMapping("/servicio/{servicioId}/dias")
    public Map<String, List<String>> getDiasDisponiblesPorServicio(@PathVariable Long servicioId) {
        List<String> dias = citaService.obtenerDiasPorServicio(servicioId);
        return Map.of("diasSemana", dias);
    }


}
