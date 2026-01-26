package com.example.demo.repository;

import com.example.demo.domain.Cita;
import com.example.demo.domain.Cliente;
import com.example.demo.domain.Grupo;
import com.example.demo.domain.HorarioSemanal;
import com.example.demo.domain.Servicio;
import com.example.demo.domain.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    Optional<Cita> findById(long id);

    List<Cita> findByCliente(Cliente cliente);

    List<Cita> findByHorario(HorarioSemanal horario);

    List<Cita> findByFecha(LocalDate fecha);

    boolean existsByCliente_IdAndHorario_IdAndFecha(Long clienteId, Long horarioId, LocalDate fecha);

    boolean existsByHorario_Id(Long horarioId);

    List<Cita> findByEstado(EstadoCita estado);

    List<Cita> findByFechaAndEstado(LocalDate fecha, EstadoCita estado);

    List<Cita> findByClienteAndFecha(Cliente cliente, LocalDate fecha);

    List<Cita> findByHorario_Servicio(Servicio servicio);

    List<Cita> findByHorario_Grupo(Grupo grupo);

    List<Cita> findByHorario_ServicioAndFecha(Servicio servicio, LocalDate fecha);


    // ⭐ Plazas ocupadas SOLO en ese bloque
    @Query("""
        SELECT COUNT(c)
        FROM Cita c
        WHERE c.horario = :horario
        AND c.fecha = :fecha
        AND c.horaInicio = :horaInicio
        AND c.estado = com.example.demo.domain.EstadoCita.CONFIRMADO
    """)
    long countCitasEnBloque(
            @Param("horario") HorarioSemanal horario,
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio
    );

    // ⭐ Citas activas del día
    @Query("""
        SELECT COUNT(c) 
        FROM Cita c 
        WHERE c.horario = :horario 
        AND c.fecha = :fecha 
        AND c.estado = com.example.demo.domain.EstadoCita.CONFIRMADO
    """)
    long countCitasActivas(
            @Param("horario") HorarioSemanal horario,
            @Param("fecha") LocalDate fecha
    );

    // ⭐ Para limpieza automática
    List<Cita> findByEstadoAndFechaBefore(EstadoCita estado, LocalDate fecha);
}
