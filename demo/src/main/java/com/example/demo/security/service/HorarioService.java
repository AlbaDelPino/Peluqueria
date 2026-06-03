package com.example.demo.security.service;

import com.example.demo.domain.CursoEscolar;
import com.example.demo.domain.HorarioSemanal;
import com.example.demo.domain.Servicio;
import com.example.demo.domain.Grupo;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface HorarioService {

    List<HorarioSemanal> findAll();
    Optional<HorarioSemanal> findById(long id);

    List<HorarioSemanal> findByServicio_IdServicio(Long idServicio);
    List<HorarioSemanal> findByGrupo(Grupo grupo);

    // 🔹 Buscar por todos los campos de la clave única
    List<HorarioSemanal> findByServicioAndHoraInicioAndHoraFinAndDiaSemanaAndGrupoAndCurso(
            Servicio servicio,
            LocalTime horaInicio,
            LocalTime horaFin,
            String diaSemana,
            Grupo grupo,
            CursoEscolar curso
    );
    List<HorarioSemanal> findByDiaSemanaAndServicio_IdServicio(String diaSemana, Long idServicio);


    // 🔹 Buscar por día de semana
    List<HorarioSemanal> findByDiaSemana(String diaSemana);

    // 🔹 Buscar por hora de inicio
    List<HorarioSemanal> findByHoraInicio(LocalTime horaInicio);

    HorarioSemanal addHorario(HorarioSemanal horario);
    HorarioSemanal modifyHorario(long id, HorarioSemanal horario);
    void deleteHorario(long id);

    boolean importHorarios(List<HorarioSemanal> horarios);
}
