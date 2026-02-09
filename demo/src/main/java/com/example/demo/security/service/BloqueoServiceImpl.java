package com.example.demo.security.service;

import com.example.demo.domain.*;
import com.example.demo.repository.BloqueoRepository;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class BloqueoServiceImpl implements BloqueoService {

    @Autowired
    private BloqueoRepository bloqueoRepository;
    @Autowired
    private HorarioRepository horarioRepository;
    @Autowired
    private CitaRepository citaRepository;

    @Override
    public List<BloqueoHorario> findAll() {
        return bloqueoRepository.findAll();
    }
    @Override
    public Optional<BloqueoHorario> findById(long id) {
        return bloqueoRepository.findById(id);
    }

    @Override
    public List<BloqueoHorario> findByFecha(LocalDate fecha) {
        return bloqueoRepository.findByFecha(fecha);
    }

    @Override
    public List<BloqueoHorario> findByHorarios(HorarioSemanal horario) {
        return bloqueoRepository.findByHorarios(horario);
    }

    @Override
    public List<BloqueoHorario> findByRecurrente(boolean recurrente) {
        return bloqueoRepository.findByRecurrente(recurrente);
    }

    @Override
    public BloqueoHorario addBloqueoHorario(BloqueoHorario bloqueo) {
        List<HorarioSemanal> horarios = bloqueo.getHorarios();
        for(int i = 0; i < horarios.size(); i++){
            Long horarioId = horarios.get(i).getId();
            HorarioSemanal horario = horarioRepository.findById(horarioId)
                    .orElseThrow(() -> new RuntimeException("Horario no encontrado con ID: " + horarioId));

            LocalDateTime fechaHoraCita = LocalDateTime.of(bloqueo.getFecha(), LocalTime.from(LocalDateTime.now()));
            if (fechaHoraCita.isBefore(LocalDateTime.now())) {
                throw new RuntimeException("No puedes bloquear una horario en una fecha u hora pasada.");
            }

            String diaSemana = bloqueo.getFecha()
                    .format(DateTimeFormatter.ofPattern("EEEE", new Locale("es", "ES")))
                    .toUpperCase();
            if (!diaSemana.equalsIgnoreCase(horario.getDiaSemana())) {
                throw new RuntimeException("La fecha no coincide con el día del horario.");
            }

            List<Cita> citasACancelar = citaRepository.citasACancelar(horario,bloqueo.getFecha());
            if (citasACancelar.size()>0) {
                throw new RuntimeException("El cliente ya tiene una cita en esta hora.");
            }
            bloqueo.addHorario(horario);
        }
        return bloqueoRepository.save(bloqueo);
    }

    @Override
    public BloqueoHorario modifyHorariosEnBloqueo(long id, List<HorarioSemanal> newHorarios) {
        BloqueoHorario bloqueo = bloqueoRepository.findById(id).orElseThrow(() -> new RuntimeException("No se ha encontrado el bloqueo horario."));
        bloqueo.setHorarios(new ArrayList<HorarioSemanal>());
        for(int i = 0; i < newHorarios.size(); i++){
            Long horarioId = newHorarios.get(i).getId();
            HorarioSemanal horario = horarioRepository.findById(horarioId)
                    .orElseThrow(() -> new RuntimeException("Horario no encontrado con ID: " + horarioId));

            LocalDateTime fechaHoraCita = LocalDateTime.of(bloqueo.getFecha(), LocalTime.from(LocalDateTime.now()));
            if (fechaHoraCita.isBefore(LocalDateTime.now())) {
                throw new RuntimeException("No puedes bloquear una horario en una fecha u hora pasada.");
            }

            String diaSemana = bloqueo.getFecha()
                    .format(DateTimeFormatter.ofPattern("EEEE", new Locale("es", "ES")))
                    .toUpperCase();
            if (!diaSemana.equalsIgnoreCase(horario.getDiaSemana())) {
                throw new RuntimeException("La fecha no coincide con el día del horario.");
            }

            List<Cita> citasACancelar = citaRepository.citasACancelar(horario,bloqueo.getFecha());
            if (citasACancelar.size()>0) {
                throw new RuntimeException("El cliente ya tiene una cita en esta hora.");
            }
            bloqueo.addHorario(horario);
        }
        return bloqueoRepository.save(bloqueo);
    }

    @Override
    public void deleteBloqueoHorario(long id) {
        BloqueoHorario bloqueo = bloqueoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloqueo horario no encontrado"));

        bloqueoRepository.deleteById(id);
    }
}
