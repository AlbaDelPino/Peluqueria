package com.example.demo.security.service;

import com.example.demo.domain.*;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CitaServiceImpl implements CitaService {

    @Autowired private CitaRepository citaRepository;
    @Autowired private HorarioRepository horarioRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private JavaMailSender mailSender;

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
    public List<Cita> findByEstado(EstadoCita estado) {
        return citaRepository.findByEstado(estado);
    }

    @Override
    public List<Cita> findByCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return citaRepository.findByCliente(cliente);
    }

    // ⭐⭐⭐ CREAR CITA POR BLOQUES ⭐⭐⭐
    @Override
    @Transactional
    public Cita addCita(Cita cita) {

        if (cita.getCliente() == null || cita.getCliente().getId() == null) {
            throw new RuntimeException("Debes enviar un cliente con ID");
        }

        Long horarioId = cita.getHorario().getId();
        HorarioSemanal horario = horarioRepository.findById(horarioId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con ID: " + horarioId));

        // Validar fecha pasada
        LocalDateTime fechaHoraCita = LocalDateTime.of(cita.getFecha(), cita.getHoraInicio());
        if (fechaHoraCita.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No puedes reservar una cita en una fecha u hora pasada.");
        }

        // Validar día de la semana
        String diaSemana = cita.getFecha()
                .format(DateTimeFormatter.ofPattern("EEEE", new Locale("es", "ES")))
                .toUpperCase();

        if (!diaSemana.equalsIgnoreCase(horario.getDiaSemana())) {
            throw new RuntimeException("La fecha no coincide con el día del horario.");
        }

        long duracion = horario.getServicio().getDuracion();
        LocalTime horaInicio = cita.getHoraInicio();
        LocalTime horaFin = horaInicio.plusMinutes(duracion);

        // Validar que cabe dentro del horario
        if (horaFin.isAfter(horario.getHoraFin())) {
            throw new RuntimeException("La cita no cabe dentro del horario. No hay tiempo suficiente.");
        }

        // ⭐ VALIDAR QUE LA HORA ES UN BLOQUE VÁLIDO
        Map<String, Integer> bloques = horasDisponibles(horario.getId(), cita.getFecha());
        if (!bloques.containsKey(horaInicio.toString())) {
            throw new RuntimeException("La hora seleccionada no es válida para este horario.");
        }

        // ⭐ VALIDAR QUE EL CLIENTE NO TENGA YA UNA CITA EN ESA HORA
        List<Cita> citasCliente = citaRepository.findByClienteAndFecha(cita.getCliente(), cita.getFecha());
        boolean yaTieneMismaHora = citasCliente.stream()
                .anyMatch(c -> c.getHoraInicio().equals(horaInicio));

        if (yaTieneMismaHora) {
            throw new RuntimeException("El cliente ya tiene una cita en esta hora.");
        }

        // ⭐ VALIDAR PLAZAS DEL BLOQUE
        long ocupadasEnBloque = citaRepository.countCitasEnBloque(
                horario,
                cita.getFecha(),
                horaInicio
        );

        if (ocupadasEnBloque >= horario.getPlazas()) {
            throw new RuntimeException("No quedan plazas disponibles para este bloque horario.");
        }

        // Estado inicial
        cita.setEstado(EstadoCita.CONFIRMADO);

        // Guardar
        Cita guardada = citaRepository.save(cita);

        // Enviar correo
        try {
            Cliente clienteCompleto = clienteRepository.findById(cita.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            enviarCorreoConfirmacion(clienteCompleto, guardada);

        } catch (Exception e) {
            System.err.println("La reserva se hizo pero el correo falló: " + e.getMessage());
        }

        return guardada;
    }

    // ⭐⭐⭐ ELIMINAR CITA ⭐⭐⭐
    @Override
    @Transactional
    public void deleteCita(long id) {

        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        // Solo se puede eliminar si está cancelada o completada
        if (cita.getEstado() != EstadoCita.CANCELADO &&
                cita.getEstado() != EstadoCita.COMPLETADO) {

            throw new RuntimeException("Solo puedes eliminar citas canceladas o completadas.");
        }

        citaRepository.deleteById(id);
    }

    // ⭐⭐⭐ CAMBIAR ESTADO ⭐⭐⭐
    @Override
    @Transactional
    public Cita cambiarEstado(long id, EstadoCita nuevoEstado) {

        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe la cita"));

        // Solo se puede cancelar o completar
        if (nuevoEstado != EstadoCita.CANCELADO && nuevoEstado != EstadoCita.COMPLETADO) {
            throw new RuntimeException("Estado no permitido. Solo se puede cancelar o completar la cita.");
        }

        cita.setEstado(nuevoEstado);

        return citaRepository.save(cita);
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

    @Override
    public Long citasDisponibles(Long horarioId, LocalDate fecha) {
        HorarioSemanal hor = horarioRepository.findById(horarioId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        Long ocupadas = citaRepository.countCitasActivas(hor, fecha);
        return hor.getPlazas() - ocupadas;
    }

    // ⭐⭐⭐ HORAS DISPONIBLES POR BLOQUE ⭐⭐⭐
    @Override
    public Map<String, Integer> horasDisponibles(Long horarioId, LocalDate fecha) {

        HorarioSemanal horario = horarioRepository.findById(horarioId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        long duracion = horario.getServicio().getDuracion();
        LocalTime inicio = horario.getHoraInicio();
        LocalTime fin = horario.getHoraFin();

        Map<String, Integer> resultado = new LinkedHashMap<>();

        LocalTime bloque = inicio;

        while (!bloque.plusMinutes(duracion).isAfter(fin)) {

            long ocupadas = citaRepository.countCitasEnBloque(horario, fecha, bloque);
            int plazasDisponibles = (int) (horario.getPlazas() - ocupadas);

            // ⭐ SIEMPRE AÑADIMOS LA HORA, TENGA O NO PLAZAS
            resultado.put(bloque.toString(), plazasDisponibles);

            bloque = bloque.plusMinutes(duracion);
        }

        return resultado;
    }

    @Override
    public Map<String, Integer> horasConPlazasDisponibles(Long horarioId, LocalDate fecha) {

        HorarioSemanal horario = horarioRepository.findById(horarioId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        long duracion = horario.getServicio().getDuracion();
        LocalTime inicio = horario.getHoraInicio();
        LocalTime fin = horario.getHoraFin();

        Map<String, Integer> resultado = new LinkedHashMap<>();

        LocalTime bloque = inicio;

        while (!bloque.plusMinutes(duracion).isAfter(fin)) {

            long ocupadas = citaRepository.countCitasEnBloque(horario, fecha, bloque);
            int plazasDisponibles = (int) (horario.getPlazas() - ocupadas);

            // ⭐ SOLO AÑADIR SI HAY PLAZAS
            if (plazasDisponibles > 0) {
                resultado.put(bloque.toString(), plazasDisponibles);
            }

            bloque = bloque.plusMinutes(duracion);
        }

        return resultado;
    }


    private void enviarCorreoConfirmacion(Cliente cliente, Cita cita) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(cliente.getEmail());
        message.setSubject("Reserva Confirmada - Bernat Experience");

        String texto = "Hola " + cliente.getNombre() + ",\n\n" +
                "Tu cita ha sido confirmada:\n" +
                "- Servicio: " + cita.getHorario().getServicio().getNombre() + "\n" +
                "- Fecha: " + cita.getFecha() + "\n" +
                "- Hora: " + cita.getHoraInicio() + "\n\n" +
                "¡Gracias por confiar en nosotros!";

        message.setText(texto);
        mailSender.send(message);
    }

    @Override
    public List<Cita> findCitasDeHoy() {
        return citaRepository.findByFecha(LocalDate.now());
    }
}
