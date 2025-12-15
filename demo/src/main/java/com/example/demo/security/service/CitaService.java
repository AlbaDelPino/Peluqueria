package com.example.demo.security.service;

import com.example.demo.domain.Cita;
import com.example.demo.domain.Cliente;
import com.example.demo.domain.Grupo;
import com.example.demo.domain.Servicio;
import com.example.demo.domain.EstadoCita;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface CitaService {

    List<Cita> findAll();
    Optional<Cita> findById(long id);

    List<Cita> findByFecha(LocalDate fecha);
    List<Cita> findByHora(LocalTime hora);
    List<Cita> findByFechaAndHora(LocalDate fecha, LocalTime hora);

    List<Cita> findByEstado(EstadoCita estado);
    List<Cita> findByCliente(Cliente cliente);
    List<Cita> findByFechaAndEstado(LocalDate fecha, EstadoCita estado);
    List<Cita> findByClienteAndFecha(Cliente cliente, LocalDate fecha);

    List<Cita> findByHorario_Servicio(Servicio servicio);
    List<Cita> findByHorario_Grupo(Grupo grupo);
    List<Cita> findByHorario_ServicioAndFecha(Servicio servicio, LocalDate fecha);

    Cita addCita(Cita cita, Long servicioId);
    void deleteCita(long id);

    // 🔹 Solo se cambia el estado
    Cita cambiarEstado(long id, EstadoCita nuevoEstado);
}
