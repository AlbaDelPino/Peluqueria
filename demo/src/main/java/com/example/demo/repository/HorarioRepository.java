package com.example.demo.repository;

import com.example.demo.domain.CursoEscolar;
import com.example.demo.domain.HorarioSemanal;
import com.example.demo.domain.Servicio;
import com.example.demo.domain.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<HorarioSemanal, Long> {

    // Buscar por servicio + horaInicio + día + grupo
    List<HorarioSemanal> findByServicioAndHoraInicioAndDiaSemanaAndGrupoAndCurso(
            Servicio servicio,
            LocalTime horaInicio,
            String diaSemana,
            Grupo grupo,
            CursoEscolar curso
    );

    // Opcionales si quieres búsquedas más simples
    List<HorarioSemanal> findByServicioAndHoraInicioAndDiaSemanaAndCurso(
            Servicio servicio,
            LocalTime horaInicio,
            String diaSemana,
            CursoEscolar curso
    );

    List<HorarioSemanal> findByDiaSemanaAndCurso_IdCurso(String diaSemana, Long idCurso);
    List<HorarioSemanal> findByServicio_IdServicioAndCurso_IdCurso(Long idServicio, Long idCurso);

    List<HorarioSemanal> findByHoraInicioAndCurso_IdCurso(LocalTime horaInicio, Long idCurso);
    List<HorarioSemanal> findByDiaSemanaAndServicio_IdServicioAndCurso_IdCurso(String diaSemana, Long idServicio, Long idCurso);

}
