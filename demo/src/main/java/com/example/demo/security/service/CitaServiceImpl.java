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
    public List<Cita> findByEstado(String estado) {
        return citaRepository.findByEstado(estado);
    }

    @Override
    public List<Cita> findByHorario(HorarioSemanal horario) {
        return citaRepository.findByHorario(horario);
    }

    @Override
    public List<Cita> findByGrupo(Grupo grupo) {
        return citaRepository.findByGrupo(grupo);
    }

    @Override
    public List<Cita> findByCliente(Cliente cliente) {
        return citaRepository.findByCliente(cliente);
    }

    @Override
    public List<Cita> findByHorarioOrGrupoOrCliente(HorarioSemanal horario, Grupo grupo, Cliente cliente) {
        return citaRepository.findByHorarioOrGrupoOrCliente(horario, grupo, cliente);
    }

    @Override
    public List<Cita> findByFechaAndEstado(LocalDate fecha, String estado) {
        return citaRepository.findByFechaAndEstado(fecha, estado);
    }

    @Override
    public List<Cita> findByFechaAndEstadoAndHorario(LocalDate fecha, String estado, HorarioSemanal horario) {
        return citaRepository.findByFechaAndEstadoAndHorario(fecha, estado, horario);
    }

    @Override
    public Cita addCita(Cita cita) {
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
        citaRepository.findById(id)
                .orElseThrow(() -> new CitaNotFoundException(id));
        citaRepository.deleteById(id);
    }
}
