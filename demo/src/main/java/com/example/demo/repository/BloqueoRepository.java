package com.example.demo.repository;

import com.example.demo.domain.BloqueoHorario;
import com.example.demo.domain.HorarioSemanal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BloqueoRepository extends JpaRepository<BloqueoHorario, Long> {

    Optional<BloqueoHorario> findById(long id);
    BloqueoHorario findByFecha(LocalDate fecha);
    List<BloqueoHorario> findByHorarios(Optional<HorarioSemanal> horario);
    List<BloqueoHorario> findByRecurrente(boolean recurrente);
    List<BloqueoHorario> findByMotivo(String  motivo);

    // Plazas ocupadas SOLO en ese bloque
    @Query("""
        SELECT b
        FROM BloqueoHorario b
        WHERE b.recurrente = true
        AND  MONTH(b.fecha) =  MONTH(:fecha)
        AND  DAY(b.fecha) =  DAY(:fecha)
    """)
    BloqueoHorario findByDiaRecurrente(
            @Param("fecha") LocalDate fecha
    );
}
