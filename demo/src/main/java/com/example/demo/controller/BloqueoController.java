package com.example.demo.controller;

import com.example.demo.domain.BloqueoHorario;
import com.example.demo.domain.Cita;
import com.example.demo.domain.EstadoCita;
import com.example.demo.domain.HorarioSemanal;
import com.example.demo.security.service.BloqueoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/bloqueos")
@CrossOrigin(origins = "*")
public class BloqueoController {

    @Autowired
    private BloqueoService bloqueoService;

    @GetMapping
    public List<BloqueoHorario> findAll() {
        return bloqueoService.findAll();
    }

    @GetMapping("/{id}")
    public BloqueoHorario findById(@PathVariable long id) {
        return bloqueoService.findById(id).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public BloqueoHorario addBloqueoHorario(@RequestBody BloqueoHorario bloqueo) {
        return bloqueoService.addBloqueoHorario(bloqueo);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deleteBloqueoHorario(@PathVariable long id) {
        bloqueoService.deleteBloqueoHorario(id);
    }

    @PutMapping("/{id}")
    public BloqueoHorario modifyHorariosEnBloqueo(
            @PathVariable long id,
            @RequestBody BloqueoHorario bloqueo) {
        return bloqueoService.modifyHorariosEnBloqueo(id,bloqueo);
    }

    @GetMapping("/fecha")
    public BloqueoHorario findByFecha(@RequestParam String fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaFormato = LocalDate.parse(fecha, formatter);
        return bloqueoService.findByFecha(fechaFormato);
    }

    @GetMapping("/dia")
    public BloqueoHorario findByDiaRecurrente(@RequestParam String fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaFormato = LocalDate.parse(fecha, formatter);
        return bloqueoService.findByDiaRecurrente(fechaFormato);
    }

    @GetMapping("/horario")
    public List<BloqueoHorario> findByHorarios(@RequestParam Long horarioId) {
        return bloqueoService.findByHorarios(horarioId);
    }

    @GetMapping("/recurrente")
    public List<BloqueoHorario> findByRecurrente(@RequestParam boolean recurrente) {
        return bloqueoService.findByRecurrente(recurrente);
    }

    @GetMapping("/motivo")
    public List<BloqueoHorario> findByMotivo(@RequestParam String motivo) {
        return bloqueoService.findByMotivo(motivo);
    }

}

