package com.example.demo.security.service;

import com.example.demo.domain.CursoEscolar;
import com.example.demo.domain.HorarioSemanal;
import com.example.demo.domain.Servicio;
import com.example.demo.domain.Grupo;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface HorarioService {

    List<HorarioSemanal> findAll(Long idCurso);
    Optional<HorarioSemanal> findById(long id);

    List<HorarioSemanal> findByServicio_IdServicioAndCurso_IdCurso(Long idServicio, Long idCurso);
    List<HorarioSemanal> findByGrupoAndCurso(Grupo grupo, CursoEscolar curso);

    // 🔹 Buscar por todos los campos de la clave única
    List<HorarioSemanal> findByServicioAndHoraInicioAndHoraFinAndDiaSemanaAndGrupoAndCurso(
            Servicio servicio,
            LocalTime horaInicio,
            LocalTime horaFin,
            String diaSemana,
            Grupo grupo,
            CursoEscolar curso
    );
    List<HorarioSemanal> findByDiaSemanaAndServicio_IdServicioAndCurso_IdCurso(String diaSemana, Long idServicio, Long idCurso);


    // 🔹 Buscar por día de semana
    List<HorarioSemanal> findByDiaSemanaAndCurso_IdCurso(String diaSemana, Long idCurso);

    // 🔹 Buscar por hora de inicio
    List<HorarioSemanal> findByHoraInicioAndCurso_IdCurso(LocalTime horaInicio, Long idCurso);

    HorarioSemanal addHorario(HorarioSemanal horario);
    HorarioSemanal modifyHorario(long id, HorarioSemanal horario);
    void deleteHorario(long id);

    boolean importHorarios(List<HorarioSemanal> horarios);
}
