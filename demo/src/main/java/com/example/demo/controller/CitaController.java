package com.example.demo.controller;

import com.example.demo.domain.Cita;
import com.example.demo.domain.EstadoCita;
import com.example.demo.security.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    @Autowired
    private CitaService citaService;

    // ⭐ Crear cita
    @PostMapping("/reservar")
    public Cita addCita(@RequestBody Cita cita) {
        return citaService.addCita(cita);
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

    // ⭐ Citas disponibles por horario y fecha
    @GetMapping("/disponible")
    public Map<String, Integer> horasConPlazasDisponibles(
            @RequestParam Long horarioId,
            @RequestParam LocalDate fecha
    ) {
        return citaService.horasDisponibles(horarioId, fecha);
    }


    // ⭐ Horas disponibles por bloques
    @GetMapping("/horas")
    public Map<String, Integer> horasDisponibles(
            @RequestParam Long horarioId,
            @RequestParam LocalDate fecha
    ) {
        return citaService.horasDisponibles(horarioId, fecha);
    }

    // ⭐ Buscar por fecha
    @GetMapping("/fecha")
    public List<Cita> findByFecha(@RequestParam LocalDate fecha) {
        return citaService.findByFecha(fecha);
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



}
