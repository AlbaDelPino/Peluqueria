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
    @Autowired private BloqueoService bloqueoRepository;
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


        BloqueoHorario bloqueo = bloqueoRepository.findByFecha(cita.getFecha());
        BloqueoHorario recurrente = bloqueoRepository.findByDiaRecurrente(cita.getFecha());
        if (bloqueo != null && bloqueo.getHorarios().contains(horario)) {
            throw new RuntimeException("No se puede crear una cita en una fecha bloqueada.");
        }
        if (recurrente != null && recurrente.getHorarios().contains(horario)){
            throw new RuntimeException("No se puede crear una cita en una fecha bloqueada.");
        }

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

        cita.setHorario(horario);
        // Guardar
        Cita guardada = citaRepository.save(cita);

        // Enviar correo
        try {
            Cliente clienteCompleto = clienteRepository.findById(cita.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            guardada.setHorario(horario);
            guardada.setCliente(clienteCompleto);

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

    //  CAMBIAR ESTADO
    @Override
    @Transactional
    public Cita cambiarEstado(long id, EstadoCita nuevoEstado) {

        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe la cita"));

        // Solo se puede cancelar o completar
        if (nuevoEstado != EstadoCita.CANCELADO && nuevoEstado != EstadoCita.COMPLETADO) {
            throw new RuntimeException("Estado no permitido. Solo se puede cancelar o completar la cita.");
        }

        LocalDateTime fechaHoraCita = LocalDateTime.of(cita.getFecha(), cita.getHoraInicio());
        if (fechaHoraCita.isAfter(LocalDateTime.now())) {
            throw new RuntimeException("No puedes completar una cita en una fecha u hora no pasada.");
        }

        cita.setEstado(nuevoEstado);

        return citaRepository.save(cita);
    }

    @Override
    public Cita cambiarFicha(long id, Cita fichaCita) {

        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe la cita"));

        if (cita.getEstado()!=EstadoCita.COMPLETADO) {
            throw new RuntimeException("No se puede comentar una cita no completada");
        }

        cita.setTratamientos(fichaCita.getTratamientos());
        cita.setProductos(fichaCita.getProductos());
        cita.setObservaciones(fichaCita.getObservaciones());

        return citaRepository.save(cita);
    }

    @Override
    public List<Cita> citasDeRango(LocalDate fechaInicio, LocalDate fechaFin) {
        return citaRepository.findByRango(fechaInicio, fechaFin);
    }

    @Override
    public List<Cita> citasACancelar(HorarioSemanal horario, LocalDate fecha) {
        return citaRepository.citasACancelar(horario, fecha);
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
        try {
            // 1. Verificación de seguridad (ya la tienes, ¡bien!)
            if (cita.getHorario() == null || cita.getHorario().getServicio() == null) {
                System.err.println("❌ No se puede enviar correo: Datos de servicio incompletos.");
                return;
            }

            SimpleMailMessage message = new SimpleMailMessage();

            // 2. IMPORTANTE: Especificar el remitente (tu cuenta de gmail)
            message.setFrom("albadelpino1@gmail.com");

            message.setTo(cliente.getEmail());
            message.setSubject("Reserva Confirmada - Bernat Experience");

            // 3. Extraer datos del objeto que vimos en Postman
            String nombreServicio = cita.getHorario().getServicio().getNombre();
            String nombreCliente = (cliente.getNombre() != null) ? cliente.getNombre() : "Cliente";

            String texto = "Hola " + nombreCliente + ",\n\n" +
                    "Tu reserva para '" + nombreServicio + "' ha sido confirmada con éxito.\n" +
                    "Fecha: " + cita.getFecha() + "\n" +
                    "Hora: " + cita.getHoraInicio() + "\n\n" +
                    "¡Gracias por confiar en nosotros!";

            message.setText(texto);

            // 4. Intento de envío
            mailSender.send(message);
            System.out.println("✅ Correo enviado correctamente a: " + cliente.getEmail());

        } catch (Exception e) {
            // Esto imprimirá el error exacto en la consola de Spring si Gmail rechaza algo
            System.err.println("⚠️ Error en el servidor de correo: " + e.getMessage());
            // e.printStackTrace(); // Descomenta esta línea si necesitas ver el rastro completo del error
        }
    }

    @Override
    public List<Cita> findCitasDeHoy() {
        return citaRepository.findByFecha(LocalDate.now());
    }


    // En CitaService.java
    public List<String> obtenerDiasPorServicio(Long servicioId) {
        // Aquí buscas en tu repositorio de Horarios filtrando por el ID del servicio
        // y mapeas para obtener solo los nombres de los días de la semana
        return horarioRepository.findByServicio_IdServicio(servicioId)
                .stream()
                .map(horario -> horario.getDiaSemana().toString()) // O el formato que prefieras
                .distinct()
                .toList();
    }
}
