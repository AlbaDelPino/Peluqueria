package com.example.demo.security.service;

import com.example.demo.domain.HorarioSemanal;
import com.example.demo.domain.Servicio;
import com.example.demo.domain.Grupo;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface HorarioService {

    List<HorarioSemanal> findAll(String curso);
    Optional<HorarioSemanal> findById(long id);

    List<HorarioSemanal> findByServicioAndCurso(Long idServicio, String curso);
    List<HorarioSemanal> findByGrupoAndCurso(Grupo grupo, String curso);

    // 🔹 Buscar por todos los campos de la clave única
    List<HorarioSemanal> findByServicioAndHoraInicioAndHoraFinAndDiaSemanaAndGrupoAndCurso(
            Servicio servicio,
            LocalTime horaInicio,
            LocalTime horaFin,
            String diaSemana,
            Grupo grupo,
            String curso
    );
    List<HorarioSemanal> findByDiaSemanaAndServicioAndCurso(String diaSemana, Long idServicio, String curso);


    // 🔹 Buscar por día de semana
    List<HorarioSemanal> findByDiaSemanaAndCurso(String diaSemana, String curso);

    // 🔹 Buscar por hora de inicio
    List<HorarioSemanal> findByHoraInicioAndCurso(LocalTime horaInicio, String curso);

    HorarioSemanal addHorario(HorarioSemanal horario);
    HorarioSemanal modifyHorario(long id, HorarioSemanal horario);
    void deleteHorario(long id);
}
