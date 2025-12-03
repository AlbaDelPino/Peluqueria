package com.example.demo.repository;

import com.example.demo.domain.Cita;
import com.example.demo.domain.Cliente;
import com.example.demo.domain.Grupo;
import com.example.demo.domain.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    // Buscar citas por cliente
    List<Cita> findByCliente(Cliente cliente);

    // Buscar citas por alumno/grupo
    List<Cita> findByAlumno(Grupo alumno);

    // Buscar citas por servicio
    List<Cita> findByServicio(Servicio servicio);

    // Buscar citas por fecha
    List<Cita> findByFecha(LocalDate fecha);

    // Buscar citas por estado
    List<Cita> findByEstado(String estado);
}
