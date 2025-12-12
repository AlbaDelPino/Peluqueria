package com.example.demo.security.service;

import com.example.demo.domain.Cita;
import com.example.demo.domain.Cliente;
import com.example.demo.domain.Grupo;
import com.example.demo.domain.HorarioSemanal;
import com.example.demo.domain.Servicio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CitaService {

    List<Cita> findAll();
    Optional<Cita> findById(long id);

    List<Cita> findByFecha(LocalDate fecha);
    List<Cita> findByEstado(boolean estado);
    List<Cita> findByHorario(HorarioSemanal horario);
    List<Cita> findByCliente(Cliente cliente);

    List<Cita> findByFechaAndEstado(LocalDate fecha, boolean estado);
    List<Cita> findByFechaAndEstadoAndHorario(LocalDate fecha, boolean estado, HorarioSemanal horario);

    List<Cita> findByClienteAndFecha(Cliente cliente, LocalDate fecha);

    Cita addCita(Cita cita);
    Cita modifyCita(long id, Cita cita);
    void deleteCita(long id);

    // 🔹 Método extra para cambiar estado (confirmar/cancelar)
    Cita cambiarEstado(long id, boolean nuevoEstado);
}
