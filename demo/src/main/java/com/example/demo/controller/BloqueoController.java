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
    public BloqueoHorario findById(long id) {
        return bloqueoService.findById(id).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public BloqueoHorario addBloqueoHorario(BloqueoHorario bloqueo) {
        return bloqueoService.addBloqueoHorario(bloqueo);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deleteBloqueoHorario(long id) {
        bloqueoService.deleteBloqueoHorario(id);
    }

    @PutMapping("/{id}")
    public BloqueoHorario modifyHorariosEnBloqueo(long id, List<HorarioSemanal> newHorarios) {
        return bloqueoService.modifyHorariosEnBloqueo(id,newHorarios);
    }

    @GetMapping("/fecha")
    public BloqueoHorario findByFecha(LocalDate fecha) {
        return bloqueoService.findByFecha(fecha);
    }

    @GetMapping("/dia")
    public boolean findByDiaRecurrente(LocalDate fecha) {
        return bloqueoService.findByDiaRecurrente(fecha);
    }

    @GetMapping("/horario")
    public List<BloqueoHorario> findByHorarios(HorarioSemanal horario) {
        return bloqueoService.findByHorarios(horario);
    }

    @GetMapping("/recurrente")
    public List<BloqueoHorario> findByRecurrente(boolean recurrente) {
        return bloqueoService.findByRecurrente(recurrente);
    }

}

