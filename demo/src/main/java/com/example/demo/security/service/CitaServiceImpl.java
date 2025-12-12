package com.example.demo.security.service;

import com.example.demo.domain.*;
import com.example.demo.exception.CitaNotFoundException;
import com.example.demo.exception.HorarioNotFoundException;
import com.example.demo.exception.ServicioNotFoundException;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CitaServiceImpl implements CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    @Override
    public List<Cita> findAll() {
        return citaRepository.findAll();
    }

    @Override
    public Optional<Cita> findById(long id) {
        return citaRepository.findById(id);
    }

    @Override
    public List<Cita> findByFecha(LocalDate fecha) {
        return citaRepository.findByFecha(fecha);
    }

    @Override
    public List<Cita> findByEstado(boolean estado) {
        return citaRepository.findByEstado(estado);
    }

    @Override
    public List<Cita> findByHorario(HorarioSemanal horario) {
        return citaRepository.findByHorario(horario);
    }



    @Override
    public List<Cita> findByCliente(Cliente cliente) {
        return citaRepository.findByCliente(cliente);
    }

    @Override
    public List<Cita> findByFechaAndEstado(LocalDate fecha, boolean estado) {
        return citaRepository.findByFechaAndEstado(fecha, estado);
    }

    @Override
    public List<Cita> findByFechaAndEstadoAndHorario(LocalDate fecha, boolean estado, HorarioSemanal horario) {
        return citaRepository.findByFechaAndEstadoAndHorario(fecha, estado, horario);
    }

    @Override
    public List<Cita> findByClienteAndFecha(Cliente cliente, LocalDate fecha) {
        return citaRepository.findByClienteAndFecha(cliente, fecha);
    }



    @Override
    public Cita addCita(Cita cita) {
        // 🔹 Resta plaza en el horario
        HorarioSemanal horario = cita.getHorario();
        if (horario.getPlazas() <= 0) {
            throw new RuntimeException("No quedan plazas disponibles en este horario");
        }
        horario.setPlazas(horario.getPlazas() - 1);
        horarioRepository.save(horario);

        return citaRepository.save(cita);
    }

    @Override
    public Cita modifyCita(long id, Cita newCita) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new CitaNotFoundException(id));
        newCita.setId(cita.getId());
        return citaRepository.save(newCita);
    }

    @Override
    public void deleteCita(long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new CitaNotFoundException(id));

        HorarioSemanal horario = cita.getHorario();
        if (horario != null) {
            horario.setPlazas(horario.getPlazas() + 1);
            horarioRepository.save(horario);
        }

        citaRepository.deleteById(id);
    }

    @Override
    public Cita cambiarEstado(long id, boolean nuevoEstado) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new CitaNotFoundException(id));
        cita.setEstado(nuevoEstado);
        return citaRepository.save(cita);
    }
}
