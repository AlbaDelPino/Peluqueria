package com.example.demo.controller;

import com.example.demo.domain.BloqueoHorario;
import com.example.demo.domain.Cita;
import com.example.demo.domain.EstadoCita;
import com.example.demo.domain.HorarioSemanal;
import com.example.demo.security.service.BloqueoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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
    public ResponseEntity<?> addBloqueoHorario(@RequestBody BloqueoHorario bloqueo) {
        try{
            BloqueoHorario added = bloqueoService.addBloqueoHorario(bloqueo);
            return new ResponseEntity<>(added, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Creamos el mapa con la estructura que quieres
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());

            // Devolvemos el JSON con un código 400 (Bad Request)
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deleteBloqueoHorario(@PathVariable long id) {
        bloqueoService.deleteBloqueoHorario(id);
    }

    @PutMapping
    public BloqueoHorario modifyHorariosEnBloqueo(@RequestBody BloqueoHorario bloqueo) {
        return bloqueoService.modifyHorariosEnBloqueo(bloqueo);
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
    @GetMapping("/recurrenteycurso")
    public List<BloqueoHorario> findNoRecurrentesByCursoSeleccionadoOrRecurrente() {
        return bloqueoService.findNoRecurrentesByCursoSeleccionadoOrRecurrente(true);
    }

}

