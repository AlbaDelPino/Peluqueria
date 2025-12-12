package com.example.demo.repository;

import com.example.demo.domain.Cita;
import com.example.demo.domain.Cliente;
import com.example.demo.domain.Grupo;
import com.example.demo.domain.HorarioSemanal;
import com.example.demo.domain.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    Optional<Cita> findById(long id);

    List<Cita> findByCliente(Cliente cliente);



    List<Cita> findByHorario(HorarioSemanal horario);



    List<Cita> findByFecha(LocalDate fecha);

    // 🔹 Buscar por estado (true = confirmada, false = cancelada)
    List<Cita> findByEstado(boolean estado);

    List<Cita> findByFechaAndEstado(LocalDate fecha, boolean estado);

    List<Cita> findByFechaAndEstadoAndHorario(LocalDate fecha, boolean estado, HorarioSemanal horario);

    List<Cita> findByClienteAndFecha(Cliente cliente, LocalDate fecha);

}
