package com.example.demo.repository;

import com.example.demo.domain.BloqueoHorario;
import com.example.demo.domain.Cita;
import com.example.demo.domain.Cliente;
import com.example.demo.domain.HorarioSemanal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BloqueoRepository extends JpaRepository<BloqueoHorario, Long> {

    Optional<BloqueoHorario> findById(long id);
    BloqueoHorario findByFecha(LocalDate fecha);
    List<BloqueoHorario> findByHorarios(HorarioSemanal horario);
    List<BloqueoHorario> findByRecurrente(boolean recurrente);
    boolean findByDiaRecurrente(LocalDate fecha);
}
