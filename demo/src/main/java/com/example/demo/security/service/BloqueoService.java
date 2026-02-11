package com.example.demo.security.service;

import com.example.demo.domain.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BloqueoService {

    List<BloqueoHorario> findAll();
    Optional<BloqueoHorario> findById(long id);
    BloqueoHorario findByFecha(LocalDate fecha);
    BloqueoHorario findByDiaRecurrente(LocalDate fecha);
    List<BloqueoHorario> findByHorarios(HorarioSemanal horario);
    List<BloqueoHorario> findByRecurrente(boolean recurrente);



    BloqueoHorario addBloqueoHorario(BloqueoHorario bloqueo);
    BloqueoHorario modifyHorariosEnBloqueo(long id, List<HorarioSemanal> newHorarios);
    void deleteBloqueoHorario(long id);
}
