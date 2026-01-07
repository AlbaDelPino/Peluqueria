package com.example.demo.security.service;

import com.example.demo.domain.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CitaService {

    List<Cita> findAll();
    Optional<Cita> findById(long id);

    List<Cita> findByFecha(LocalDate fecha);



    List<Cita> findByEstado(EstadoCita estado);
    List<Cita> findByCliente(Long clienteId);
    List<Cita> findByFechaAndEstado(LocalDate fecha, EstadoCita estado);
    List<Cita> findByClienteAndFecha(Cliente cliente, LocalDate fecha);

    List<Cita> findByHorario_Servicio(Servicio servicio);
    List<Cita> findByHorario_Grupo(Grupo grupo);
    List<Cita> findByHorario_ServicioAndFecha(Servicio servicio, LocalDate fecha);
    Long citasDisponibles(Long horarioId,LocalDate fecha);


    Cita addCita(Cita cita);
    void deleteCita(long id);

    // 🔹 Solo se cambia el estado
    Cita cambiarEstado(long id, boolean nuevoEstado);

}
