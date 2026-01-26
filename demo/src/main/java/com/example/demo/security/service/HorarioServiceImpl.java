package com.example.demo.security.service;

import com.example.demo.domain.HorarioSemanal;
import com.example.demo.domain.Servicio;
import com.example.demo.domain.Grupo;
import com.example.demo.exception.HorarioNotFoundException;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.GrupoRepository;
import com.example.demo.repository.HorarioRepository;
import com.example.demo.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
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

    @Override
    public List<HorarioSemanal> findAll() {
        return horarioRepository.findAll();
    }

    @Override
    public Optional<HorarioSemanal> findById(long id) {
        return horarioRepository.findById(id);
    }

    @Override
    public List<HorarioSemanal> findByServicio(Long idServicio) {
        return horarioRepository.findByServicio_IdServicio(idServicio);
    }

    @Override
    public List<HorarioSemanal> findByDiaSemanaAndServicio(String diaSemana, Long idServicio) {
        return horarioRepository.findByDiaSemanaAndServicio_IdServicio(diaSemana.toUpperCase(), idServicio);
    }


    @Override
    public List<HorarioSemanal> findByGrupo(Grupo grupo) {
        return horarioRepository.findAll()
                .stream()
                .filter(h -> h.getGrupo().equals(grupo))
                .toList();
    }

    @Override
    public List<HorarioSemanal> findByServicioAndHoraInicioAndHoraFinAndDiaSemanaAndGrupo(
            Servicio servicio,
            LocalTime horaInicio,
            LocalTime horaFin,
            String diaSemana,
            Grupo grupo
    ) {
        return horarioRepository.findByServicioAndHoraInicioAndDiaSemanaAndGrupo(
                servicio, horaInicio, diaSemana, grupo
        );
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
    public HorarioSemanal addHorario(HorarioSemanal horario) {
        // Recuperar servicio desde BD
        Servicio servicio = servicioRepository.findById(horario.getServicio().getId_servicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        if (servicio.getDuracion()>horario.getDuracion()){
            new RuntimeException("El horario no es lo suficientemente largo para ofrecer este servicio");
        }
        horario.setServicio(servicio);
        // Recuperar grupo desde BD
        Grupo grupo = grupoRepository.findById(horario.getGrupo().getId())
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
        horario.setGrupo(grupo);
        return horarioRepository.save(horario);
    }
    @Override
    public HorarioSemanal modifyHorario(long id, HorarioSemanal newHorario) {
        HorarioSemanal horario = horarioRepository.findById(id)
                .orElseThrow(() -> new HorarioNotFoundException(id));

        // Recuperar servicio desde BD
        Servicio servicio = servicioRepository.findById(newHorario.getServicio().getId_servicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        newHorario.setServicio(servicio);

        // Recuperar grupo desde BD
        Grupo grupo = grupoRepository.findById(newHorario.getGrupo().getId())
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
        newHorario.setGrupo(grupo);

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

   /* @Override
    public void deleteHorario(long id) {
        HorarioSemanal horario = horarioRepository.findById(id)
                .orElseThrow(() -> new HorarioNotFoundException(id));
        horarioRepository.deleteById(id);
    }*/


}
