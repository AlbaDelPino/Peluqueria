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
    List<HorarioSemanal> findByServicioAndHoraInicioAndDiaSemanaAndGrupo(
            Servicio servicio,
            LocalTime horaInicio,
            String diaSemana,
            Grupo grupo
    );

    // Opcionales si quieres búsquedas más simples
    List<HorarioSemanal> findByServicioAndHoraInicioAndDiaSemana(
            Servicio servicio,
            LocalTime horaInicio,
            String diaSemana
    );

    List<HorarioSemanal> findByDiaSemana(String diaSemana);
    List<HorarioSemanal> findByServicio_IdServicio(Long idServicio);

    List<HorarioSemanal> findByHoraInicio(LocalTime horaInicio);
    List<HorarioSemanal> findByDiaSemanaAndServicio_IdServicio(String diaSemana, Long idServicio);

}
