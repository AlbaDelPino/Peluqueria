package com.example.demo.repository;

import com.example.demo.domain.Grupo;
import com.example.demo.domain.HorarioSemanal;
import com.example.demo.domain.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HorarioRepository extends JpaRepository<HorarioSemanal, Long> {

    Optional<HorarioSemanal> findById(long id);

    List<HorarioSemanal> findByPlazasGreaterThanEqual(long plazas);

    List<HorarioSemanal> findByGrupo(Grupo grupo);

    List<HorarioSemanal> findByServicio(Servicio servicio);

    List<HorarioSemanal> findByHoraInicio(LocalTime horaInicio);

    List<HorarioSemanal> findByHoraFin(LocalTime horaFin);

    List<HorarioSemanal> findByDiaSemana(String diaSemana);

    List<HorarioSemanal> findByDiaSemanaOrHoraInicio(String diaSemana, LocalTime horaInicio);

    // 🔹 Consulta compuesta para localizar horario exacto
    Optional<HorarioSemanal> findByDiaSemanaAndHoraInicioAndHoraFinAndGrupoAndServicio(
            String diaSemana,
            LocalTime horaInicio,
            LocalTime horaFin,
            Grupo grupo,
            Servicio servicio
    );
}
