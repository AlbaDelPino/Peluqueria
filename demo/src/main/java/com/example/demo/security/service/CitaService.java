package com.example.demo.security.service;

import com.example.demo.domain.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface CitaService {

    List<Cita> findAll();
    Optional<Cita> findById(long id);
    List<Cita> findByFecha(LocalDate fecha);
    List<Cita> findByEstado(String estado);
    List<Cita> findByHorario(HorarioSemanal horario);
    List<Cita> findByGrupo(Grupo grupo);
    List<Cita> findByCliente(Cliente cliente);
    List<Cita> findByHorarioOrGrupoOrCliente (HorarioSemanal horario, Grupo grupo,Cliente cliente);
    List<Cita> findByFechaAndEstado (LocalDate fecha,String estado);
    List<Cita> findByFechaAndEstadoAndHorario (LocalDate fecha,String estado,HorarioSemanal horario);

    Cita addCita(Cita cita);
    Cita modifyCita(long id, Cita cita);
    void deleteCita(long id);
}
