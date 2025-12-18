package com.example.demo.security.service;

import com.example.demo.domain.*;
import com.example.demo.exception.CitaNotFoundException;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class CitaServiceImpl implements CitaService {

    @Autowired private CitaRepository citaRepository;
    @Autowired private HorarioRepository horarioRepository;
    @Autowired private ClienteRepository clienteRepository;

    @Override
    public List<Cita> findAll() {
        return citaRepository.findAll();
    }

    @Override
    public Optional<Cita> findById(long id) {
        return citaRepository.findById(id);
    }

    // --- MÉTODOS DE BÚSQUEDA ---

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
    public List<Cita> findByCliente(Long clienteId) {
        // Buscamos al cliente para asegurarnos de que existe
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Retornamos su lista de citas
        return citaRepository.findByCliente(cliente);
    }

    // --- LÓGICA DE NEGOCIO PRINCIPAL ---
    @Override
    @Transactional
    public Cita addCitaCliente(Cita cita, Long clienteId, HorarioSemanal horario) {
        // 1. Obtener día de la semana en español para el Horario
        String diaSemana = cita.getFecha()
                .format(DateTimeFormatter.ofPattern("EEEE", new Locale("es", "ES")))
                .toUpperCase();

        // 2. VALIDACIÓN DE PLAZAS (Usando la Query)
        long ocupadas = citaRepository.countCitasActivas(horario, cita.getFecha(), EstadoCita.CANCELADO);

        if (ocupadas >= horario.getPlazas()) {
            throw new RuntimeException("Lo sentimos, no quedan plazas libres para este horario el día " + cita.getFecha());
        }

        // 3. Configurar y guardar
        cita.setHorario(horario);
        cita.setEstado(EstadoCita.PENDIENTE);
        return citaRepository.save(cita);
    }

    @Override
    @Transactional
    public Cita addCita(Cita cita, Long clienteId,HorarioSemanal horario) {
        // 1. Buscar Cliente
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
        cita.setCliente(cliente);

        // 2. Obtener día de la semana en español para el Horario
        String diaSemana = cita.getFecha()
                .format(DateTimeFormatter.ofPattern("EEEE", new Locale("es", "ES")))
                .toUpperCase();

        // 4. VALIDACIÓN DE PLAZAS (Usando la Query)
        long ocupadas = citaRepository.countCitasActivas(horario, cita.getFecha(), EstadoCita.CANCELADO);

        if (ocupadas >= horario.getPlazas()) {
            throw new RuntimeException("Lo sentimos, no quedan plazas libres para este horario el día " + cita.getFecha());
        }

        // 5. Configurar y guardar
        cita.setHorario(horario);
        cita.setEstado(EstadoCita.PENDIENTE);
        return citaRepository.save(cita);
    }

    @Override
    @Transactional
    public void deleteCita(long id) {
        if (!citaRepository.existsById(id)) throw new RuntimeException("Cita no encontrada");
        citaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Cita cambiarEstado(long id, EstadoCita nuevoEstado) {
        Cita cita = citaRepository.findById(id).orElseThrow(() -> new RuntimeException("No existe la cita"));
        cita.setEstado(nuevoEstado);
        return citaRepository.save(cita);
    }

    // --- OTROS MÉTODOS ---

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
}