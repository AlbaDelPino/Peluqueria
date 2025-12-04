package com.example.demo.service;

import com.example.demo.domain.Cita;
import com.example.demo.domain.Cliente;
import com.example.demo.domain.Grupo;
import com.example.demo.domain.Servicio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface CitaService {

    // Crear cita con IDs de entidades relacionadas
    Cita crearCita(Cita cita);
    List<Cita> obtenerTodas();

    Optional<Cita> obtenerPorId(Long id);

    Optional<Cita> actualizarCita(Long id, Cita citaDetalles);

    boolean eliminarCita(Long id);

    List<Cita> obtenerPorCliente(Cliente cliente);

    List<Cita> obtenerPorAlumno(Grupo alumno);

    List<Cita> obtenerPorServicio(Servicio servicio);

    List<Cita> obtenerPorFecha(LocalDate fecha);

    List<Cita> obtenerPorEstado(String estado);
}
