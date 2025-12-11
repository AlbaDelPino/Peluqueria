package com.example.demo.repository;

import com.example.demo.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HorarioRepository extends JpaRepository<HorarioSemanal, Long> {
    List<HorarioSemanal> findAll();
    Optional<HorarioSemanal> findById(long id);
    List<HorarioSemanal> findByPlazas(long cliente);
    List<HorarioSemanal> findByGrupo(Grupo grupo);
    List<HorarioSemanal> findByServicios(List<Servicio> servicios);
    List<HorarioSemanal> findByHoraInicio(LocalTime horaInicio);
    List<HorarioSemanal> findByHoraFin(LocalTime horaFin);
    List<HorarioSemanal> findByDiaSemana(String diaSemana);
    List<HorarioSemanal> findByDiaSemanaOrHoraInicio(String diaSemana, LocalTime horaInicio);
}