package com.example.demo.security.service;

import com.example.demo.domain.Grupo;
import com.example.demo.domain.HorarioSemanal;
import com.example.demo.domain.Servicio;
import com.example.demo.exception.HorarioNotFoundException;
import com.example.demo.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class HorarioServiceImpl implements HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Override
    public List<HorarioSemanal> findAll() {
        return horarioRepository.findAll();
    }

    @Override
    public Optional<HorarioSemanal> findById(long id) {
        return horarioRepository.findById(id);
    }

    @Override
    public List<HorarioSemanal> findByDiaSemana(String diaSemana) {
        return horarioRepository.findByDiaSemana(diaSemana);
    }

    @Override
    public List<HorarioSemanal> findByHoraInicio(LocalTime horaInicio) {
        return horarioRepository.findByHoraInicio(horaInicio);
    }

    @Override
    public List<HorarioSemanal> findByHoraFin(LocalTime horaFin) {
        return horarioRepository.findByHoraFin(horaFin);
    }

    @Override
    public List<HorarioSemanal> findByPlazasGreaterThanEqual(long plazas) {
        return horarioRepository.findByPlazasGreaterThanEqual(plazas);
    }

    @Override
    public List<HorarioSemanal> findByServicio(Servicio servicio) {
        return horarioRepository.findByServicio(servicio);
    }

    @Override
    public List<HorarioSemanal> findByGrupo(Grupo grupo) {
        return horarioRepository.findByGrupo(grupo);
    }

    @Override
    public List<HorarioSemanal> findByDiaSemanaOrHoraInicio(String diaSemana, LocalTime horaInicio) {
        return horarioRepository.findByDiaSemanaOrHoraInicio(diaSemana, horaInicio);
    }

    @Override
    public Optional<HorarioSemanal> findByDiaSemanaAndHoraInicioAndHoraFinAndGrupoAndServicio(
            String diaSemana, LocalTime horaInicio, LocalTime horaFin, Grupo grupo, Servicio servicio) {
        return horarioRepository.findByDiaSemanaAndHoraInicioAndHoraFinAndGrupoAndServicio(
                diaSemana, horaInicio, horaFin, grupo, servicio);
    }

    @Override
    public HorarioSemanal addHorario(HorarioSemanal horario) {
        return horarioRepository.save(horario);
    }

    @Override
    public HorarioSemanal modifyHorario(long id, HorarioSemanal newHorario) {
        HorarioSemanal horario = horarioRepository.findById(id)
                .orElseThrow(() -> new HorarioNotFoundException(id));
        newHorario.setId(horario.getId());
        return horarioRepository.save(newHorario);
    }

    @Override
    public void deleteHorario(long id) {
        HorarioSemanal horario = horarioRepository.findById(id)
                .orElseThrow(() -> new HorarioNotFoundException(id));
        horarioRepository.deleteById(id);
    }
}
