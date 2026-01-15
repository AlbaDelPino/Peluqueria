 package com.example.demo.security.service;

import com.example.demo.domain.*;
import com.example.demo.exception.CitaNotFoundException;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    public Cita addCita(Cita cita) {

        // 1. El cliente viene del JSON, NO se carga de la BD
        //    Solo verificamos que trae un ID
        if (cita.getCliente() == null || cita.getCliente().getId() == null) {
            throw new RuntimeException("Debes enviar un cliente con ID");
        }

        // 2. El horario también viene del JSON, pero aquí sí cargamos el real
        Long horarioId = cita.getHorario().getId();
        HorarioSemanal horario = horarioRepository.findById(horarioId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con ID: " + horarioId));

        // ❗ Validar que la fecha y hora NO estén en el pasado
        LocalDateTime fechaHoraCita = LocalDateTime.of(
                cita.getFecha(),
                horario.getHoraInicio()
        );

        if (fechaHoraCita.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No puedes reservar una cita en una fecha u hora que ya han pasado.");
        }

        String diaSemana = cita.getFecha()
                .format(DateTimeFormatter.ofPattern("EEEE", new Locale("es", "ES")))
                .toUpperCase();

        if (!diaSemana.equalsIgnoreCase(horario.getDiaSemana())){
            throw new CitaNotFoundException("La fecha no coincide con el horario de este dia ");
        }
        // 🔥 VALIDACIÓN NUEVA: evitar doble reserva en la misma hora
        boolean yaExiste = citaRepository.existsByCliente_IdAndHorario_IdAndFecha(
                cita.getCliente().getId(),
                horario.getId(),
                cita.getFecha()
        );

        if (yaExiste) { throw new RuntimeException("Ya tienes una cita reservada en esta hora."); }
        // 3. Validar plazas (citas activas != CANCELADO)
        long ocupadas = citaRepository.countCitasActivas(
                horario,
                cita.getFecha()

        );

        if (ocupadas >= horario.getPlazas()) {
            throw new RuntimeException("No quedan plazas disponibles para este horario el día " + cita.getFecha());
        }

        // 4. Asignar el horario real
        cita.setHorario(horario);

        // 5. Estado inicial
        cita.setEstado(true);
        // 6. Guardar cita (cliente se guarda tal cual viene)
        Cita citaGuardada = citaRepository.save(cita);
        // 5. CARGAR CLIENTE Y ENVIAR CORREO
        try {
            // Buscamos al cliente en la BD para tener su EMAIL y NOMBRE reales
            Cliente clienteCompleto = clienteRepository.findById(cita.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            try {
                enviarCorreoConfirmacion(clienteCompleto, citaGuardada);
            } catch (MailException e) {
                // Si el correo rebota o no existe, la app NO se detiene
                System.err.println("Error de envío: El destinatario no pudo recibir el correo.");
            }        } catch (Exception e) {
            // Importante: No lanzamos excepción para no hacer rollback de la reserva si solo falla el correo
            System.err.println("La reserva se hizo pero el correo falló: " + e.getMessage());
        }
        return citaGuardada;
    }


    @Override
    @Transactional
    public void deleteCita(long id) {
        if (!citaRepository.existsById(id)) throw new RuntimeException("Cita no encontrada");
        citaRepository.deleteById(id);
    }

     @Override
     @Transactional
     public Cita cambiarEstado(long id, boolean nuevoEstado) {
         Cita cita = citaRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("No existe la cita"));

         cita.setEstado(nuevoEstado); // true o false
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

     @Override
     public Long citasDisponibles(Long horarioId,LocalDate fecha) {
         HorarioSemanal hor = horarioRepository.findById(horarioId).orElseThrow(() -> new RuntimeException("Horario no encontrado"));
         Long ocupadas = citaRepository.countCitasActivas(hor, fecha);
         Long plazas = hor.getPlazas();
         return plazas-ocupadas;
     }

     @Autowired
     private JavaMailSender mailSender;


     // Método auxiliar para el envío
     private void enviarCorreoConfirmacion(Cliente cliente, Cita cita) {
         SimpleMailMessage message = new SimpleMailMessage();
         message.setTo(cliente.getEmail());
         message.setSubject("Reserva Confirmada - Bernat Experience");

         String texto = "Hola " + cliente.getNombre() + ",\n\n" +
                 "Tu cita ha sido confirmada correctamente:\n" +
                 "- Servicio: " + cita.getHorario().getServicio().getNombre() + "\n" +
                 "- Fecha: " + cita.getFecha() + "\n" +
                 "- Hora: " + cita.getHorario().getHoraInicio() + "\n\n" +
                 "¡Gracias por confiar en nosotros!";

         message.setText(texto);
         mailSender.send(message);
     }

     @Override
     public List<Cita> findCitasDeHoy() {
         LocalDate hoy = LocalDate.now();
         return citaRepository.findByFecha(hoy);
     }


 }