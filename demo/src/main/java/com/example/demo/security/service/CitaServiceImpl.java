package com.example.demo.security.service;

import com.example.demo.domain.Cita;
import com.example.demo.domain.Cliente;
import com.example.demo.domain.EstadoCita;
import com.example.demo.domain.Grupo;
import com.example.demo.domain.HorarioSemanal;
import com.example.demo.domain.Servicio;
import com.example.demo.exception.CitaNotFoundException;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class CitaServiceImpl implements CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    @Override
    public List<Cita> findAll() {
        return citaRepository.findAll();
    }

    @Override
    public Optional<Cita> findById(long id) {
        return citaRepository.findById(id);
    }

    @Override
    public List<Cita> findByFecha(LocalDate fecha) {
        return citaRepository.findByFecha(fecha);
    }

    @Override
    public List<Cita> findByHora(LocalTime hora) {
        return citaRepository.findByHora(hora);
    }

    @Override
    public List<Cita> findByFechaAndHora(LocalDate fecha, LocalTime hora) {
        return citaRepository.findByFechaAndHora(fecha, hora);
    }

    @Override
    public List<Cita> findByEstado(EstadoCita estado) {
        return citaRepository.findByEstado(estado);
    }

    @Override
    public List<Cita> findByCliente(Cliente cliente) {
        return citaRepository.findByCliente(cliente);
    }

    @Override
    public List<Cita> findByFechaAndEstado(LocalDate fecha, EstadoCita estado) {
        return citaRepository.findByFechaAndEstado(fecha, estado);
    }

    @Override
    public List<Cita> findByClienteAndFecha(Cliente cliente, LocalDate fecha) {
        return citaRepository.findByClienteAndFecha(cliente, fecha);
    }

    @Override
    public List<Cita> findByHorario_Servicio(Servicio servicio) {
        return citaRepository.findByHorario_Servicio(servicio);
    }

    @Override
    public List<Cita> findByHorario_Grupo(Grupo grupo) {
        return citaRepository.findByHorario_Grupo(grupo);
    }

    @Override
    public List<Cita> findByHorario_ServicioAndFecha(Servicio servicio, LocalDate fecha) {
        return citaRepository.findByHorario_ServicioAndFecha(servicio, fecha);
    }

    // 🔹 Crear cita con validación completa
    @Override
    public Cita addCita(Cita cita, Long servicioId) {
        Servicio servicio = new Servicio();
        servicio.setId_servicio(servicioId);

        // Día de la semana en español usando DateTimeFormatter
        String diaSemanaCitaStr = cita.getFecha()
                .format(DateTimeFormatter.ofPattern("EEEE", new Locale("es", "ES")))
                .toUpperCase();

        List<HorarioSemanal> horarios = horarioRepository
                .findByServicioAndHoraInicioAndDiaSemana(
                        servicio,
                        cita.getHora(),
                        diaSemanaCitaStr
                );

        if (horarios.isEmpty()) {
            throw new RuntimeException("No existe un horario para ese servicio, hora y día");
        }

        HorarioSemanal horario = horarios.get(0);

        int ocupadas = citaRepository.countByHorarioAndFechaAndHora(horario, cita.getFecha(), cita.getHora());
        if (ocupadas >= horario.getPlazas()) {
            throw new RuntimeException("No quedan plazas disponibles en este horario para ese día/hora");
        }

        cita.setHorario(horario);
        cita.setEstado(EstadoCita.PENDIENTE); // por defecto
        return citaRepository.save(cita);
    }


    @Override
    public void deleteCita(long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        HorarioSemanal horario = cita.getHorario();
        if (horario == null) {
            throw new RuntimeException("La cita no tiene horario asociado");
        }

        horario.setPlazas(horario.getPlazas() + 1);
        horarioRepository.save(horario);

        citaRepository.delete(cita);
    }

    // 🔹 Solo se cambia el estado
    @Override
    public Cita cambiarEstado(long id, EstadoCita nuevoEstado) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new CitaNotFoundException(id));

        // Si se cancela la cita, liberar la plaza en el horario
        if (nuevoEstado == EstadoCita.CANCELADO) {
            HorarioSemanal horario = cita.getHorario();
            if (horario != null) {
                horario.setPlazas(horario.getPlazas() + 1);
                horarioRepository.save(horario);
            }
        }

        cita.setEstado(nuevoEstado);
        return citaRepository.save(cita);
    }

}
