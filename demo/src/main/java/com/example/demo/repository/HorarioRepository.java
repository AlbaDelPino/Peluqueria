package com.example.demo.repository;

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
            String curso
    );

    // Opcionales si quieres búsquedas más simples
    List<HorarioSemanal> findByServicioAndHoraInicioAndDiaSemanaAndCurso(
            Servicio servicio,
            LocalTime horaInicio,
            String diaSemana,
            String curso
    );

    List<HorarioSemanal> findByDiaSemanaAndCurso(String diaSemana,String curso);
    List<HorarioSemanal> findByServicio_IdServicioAndCurso(Long idServicio,String curso);

    List<HorarioSemanal> findByHoraInicioAndCurso(LocalTime horaInicio, String curso);
    List<HorarioSemanal> findByDiaSemanaAndServicio_IdServicioAndCurso(String diaSemana, Long idServicio, String curso);

}
