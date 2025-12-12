package com.example.demo.security.service;

import com.example.demo.domain.Grupo;
import com.example.demo.domain.HorarioSemanal;
import com.example.demo.domain.Servicio;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface HorarioService {

    List<HorarioSemanal> findAll();
    Optional<HorarioSemanal> findById(long id);

    List<HorarioSemanal> findByDiaSemana(String diaSemana);
    List<HorarioSemanal> findByHoraInicio(LocalTime horaInicio);
    List<HorarioSemanal> findByHoraFin(LocalTime horaFin);
    List<HorarioSemanal> findByPlazasGreaterThanEqual(long plazas);
    List<HorarioSemanal> findByServicio(Servicio servicio);

    List<HorarioSemanal> findByGrupo(Grupo grupo);
    List<HorarioSemanal> findByDiaSemanaOrHoraInicio(String diaSemana, LocalTime horaInicio);

    Optional<HorarioSemanal> findByDiaSemanaAndHoraInicioAndHoraFinAndGrupoAndServicio(
            String diaSemana,
            LocalTime horaInicio,
            LocalTime horaFin,
            Grupo grupo,
            Servicio servicio
    );

    HorarioSemanal addHorario(HorarioSemanal horario);
    HorarioSemanal modifyHorario(long id, HorarioSemanal newHorario);
    void deleteHorario(long id);
}
