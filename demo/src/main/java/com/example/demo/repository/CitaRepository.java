package com.example.demo.repository;

import com.example.demo.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findAll();
    Optional<Cita> findById(long id);

    List<Cita> findByCliente(Cliente cliente);
    List<Cita> findByGrupo(Grupo grupo);
    List<Cita> findByHorario(HorarioSemanal horario);
    List<Cita> findByFecha(LocalDate fecha);
    List<Cita> findByEstado(String estado);
    List<Cita> findByFechaAndEstado(LocalDate fecha, String estado);
    List<Cita> findByFechaAndEstadoAndHorario(LocalDate fecha, String estado, HorarioSemanal horario);
    List<Cita> findByHorarioOrGrupoOrCliente (HorarioSemanal horario, Grupo grupo,Cliente cliente);
}
