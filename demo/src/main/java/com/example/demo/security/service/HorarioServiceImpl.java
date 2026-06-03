package com.example.demo.security.service;

import com.example.demo.domain.CursoEscolar;
import com.example.demo.domain.HorarioSemanal;
import com.example.demo.domain.Servicio;
import com.example.demo.domain.Grupo;
import com.example.demo.exception.HorarioNotFoundException;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class HorarioServiceImpl implements HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private CursoRepository cursoRepository;


    @Override
    public List<HorarioSemanal> findAll() {
        return horarioRepository.findByCurso_Seleccionado(true);
    }

    @Override
    public Optional<HorarioSemanal> findById(long id) {
        return horarioRepository.findById(id);
    }

    public List<HorarioSemanal> findByServicio_IdServicio(Long idServicio) {
        return horarioRepository.findByServicio_IdServicioAndCurso_Seleccionado(idServicio, true);
    }

    public List<HorarioSemanal> findByDiaSemanaAndServicio_IdServicio(String diaSemana, Long idServicio) {
        return horarioRepository.findByDiaSemanaAndServicio_IdServicioAndCurso_Seleccionado(diaSemana.toUpperCase(), idServicio,true);
    }


    @Override
    public List<HorarioSemanal> findByGrupo(Grupo grupo) {
        return horarioRepository.findByCurso_Seleccionado(true)
                .stream()
                .filter(h -> h.getGrupo().equals(grupo))
                .toList();
    }

    @Override
    public List<HorarioSemanal> findByServicioAndHoraInicioAndHoraFinAndDiaSemanaAndGrupoAndCurso(
            Servicio servicio,
            LocalTime horaInicio,
            LocalTime horaFin,
            String diaSemana,
            Grupo grupo,
            CursoEscolar curso
    ) {
        return horarioRepository.findByServicioAndHoraInicioAndDiaSemanaAndGrupoAndCurso(
                servicio, horaInicio, diaSemana, grupo, curso
        );
    }

    @Override
    public List<HorarioSemanal> findByDiaSemana(String diaSemana) {
        return horarioRepository.findByDiaSemanaAndCurso_Seleccionado(diaSemana,true);
    }

    @Override
    public List<HorarioSemanal> findByHoraInicio(LocalTime horaInicio) {
        return horarioRepository.findByHoraInicioAndCurso_Seleccionado(horaInicio,true);
    }

    @Override
    public HorarioSemanal addHorario(HorarioSemanal horario) {
        // Recuperar servicio desde BD
        Servicio servicio = servicioRepository.findById(horario.getServicio().getId_servicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        long until = horario.getHoraInicio().until(horario.getHoraFin(), ChronoUnit.MINUTES);
        if (servicio.getDuracion()>until){
            throw new RuntimeException("El horario no es lo suficientemente largo para ofrecer este servicio");
        }

        horario.setServicio(servicio);

        // Recuperar grupo desde BD
        Grupo grupo = grupoRepository.findById(horario.getGrupo().getId())
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
        horario.setGrupo(grupo);

        // Recuperar curso desde BD
        Long cursoId = horario.getCurso().getIdCurso();
        CursoEscolar curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + cursoId));
        horario.setCurso(curso);

        return horarioRepository.save(horario);
    }
    @Override
    public HorarioSemanal modifyHorario(long id, HorarioSemanal newHorario) {
        HorarioSemanal horario = horarioRepository.findById(id)
                .orElseThrow(() -> new HorarioNotFoundException(id));

        // Recuperar servicio desde BD
        Servicio servicio = servicioRepository.findById(newHorario.getServicio().getId_servicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        long until = horario.getHoraInicio().until(horario.getHoraFin(), ChronoUnit.MINUTES);
        if (servicio.getDuracion()>until){
            throw new RuntimeException("El horario no es lo suficientemente largo para ofrecer este servicio");
        }

        newHorario.setServicio(servicio);

        // Recuperar grupo desde BD
        Grupo grupo = grupoRepository.findById(newHorario.getGrupo().getId())
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
        newHorario.setGrupo(grupo);

        // Recuperar curso desde BD
        CursoEscolar curso = cursoRepository.findById(newHorario.getCurso().getIdCurso())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        newHorario.setCurso(curso);

        newHorario.setId(horario.getId());
        return horarioRepository.save(newHorario);
    }


    // En HorarioServiceImpl.java
    @Override
    public void deleteHorario(long id) {
        // ELIMINA cualquier "if" o "throw new RuntimeException" que tengas aquí
        // El "cascade = CascadeType.ALL" que pusimos arriba se encarga de todo
        horarioRepository.deleteById(id);
    }

    @Override
    public boolean importHorarios(List<HorarioSemanal> horarios) {
        for(HorarioSemanal h : horarios) {
            Servicio servicio = servicioRepository.findById(h.getServicio().getId_servicio())
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

            long until = h.getHoraInicio().until(h.getHoraFin(), ChronoUnit.MINUTES);
            if (servicio.getDuracion() > until) {
                throw new RuntimeException("El horario no es lo suficientemente largo para ofrecer este servicio");
            }
            h.setServicio(servicio);

            Grupo grupo = grupoRepository.findById(h.getGrupo().getId())
                    .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
            h.setGrupo(grupo);

            CursoEscolar curso = cursoRepository.findById(h.getCurso().getIdCurso())
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
            h.setCurso(curso);

            horarioRepository.save(h);
        }
        return true;
    }

   /* @Override
    public void deleteHorario(long id) {
        HorarioSemanal horario = horarioRepository.findById(id)
                .orElseThrow(() -> new HorarioNotFoundException(id));
        horarioRepository.deleteById(id);
    }*/


}
