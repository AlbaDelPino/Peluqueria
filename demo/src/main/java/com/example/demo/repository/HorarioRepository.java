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

    List<HorarioSemanal> findByDiaSemanaAndCurso_Seleccionado(String diaSemana,boolean selecionado);
    List<HorarioSemanal> findByServicio_IdServicioAndCurso_Seleccionado(Long idServicio, boolean selecionado);

    List<HorarioSemanal> findByHoraInicioAndCurso_Seleccionado(LocalTime horaInicio, boolean selecionado);
    List<HorarioSemanal> findByDiaSemanaAndServicio_IdServicioAndCurso_Seleccionado(String diaSemana, Long idServicio, boolean selecionado);

    List<HorarioSemanal> findByCurso_Seleccionado(boolean selecionado);
}
