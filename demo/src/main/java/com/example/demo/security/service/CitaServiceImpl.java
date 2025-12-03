package com.example.demo.security.service;

import com.example.demo.domain.Cita;
import com.example.demo.domain.Cliente;
import com.example.demo.domain.Grupo;
import com.example.demo.domain.Servicio;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.GrupoRepository;
import com.example.demo.repository.ServicioRepository;
import com.example.demo.service.CitaService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
@Service
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final ServicioRepository servicioRepository;
    private final ClienteRepository clienteRepository;
    private final GrupoRepository grupoRepository;

    public CitaServiceImpl(CitaRepository citaRepository,
                           ServicioRepository servicioRepository,
                           ClienteRepository clienteRepository,
                           GrupoRepository grupoRepository) {
        this.citaRepository = citaRepository;
        this.servicioRepository = servicioRepository;
        this.clienteRepository = clienteRepository;
        this.grupoRepository = grupoRepository;
    }

    @Override
    public Cita crearCita(Cita cita) {
        Servicio servicio = servicioRepository.findById(cita.getServicio().getId_servicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        Cliente cliente = clienteRepository.findById(cita.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Grupo alumno = grupoRepository.findById(cita.getAlumno().getId())
                .orElseThrow(() -> new RuntimeException("Alumno/Grupo no encontrado"));

        cita.setServicio(servicio);
        cita.setCliente(cliente);
        cita.setAlumno(alumno);

        return citaRepository.save(cita);
    }

    @Override
    public List<Cita> obtenerTodas() {
        return citaRepository.findAll();
    }

    @Override
    public Optional<Cita> obtenerPorId(Long id) {
        return citaRepository.findById(id);
    }
    @Override
    public Optional<Cita> actualizarCita(Long id, Cita citaDetalles) {
        return citaRepository.findById(id).map(cita -> {
            cita.setFecha(citaDetalles.getFecha());
            cita.setHoraInicio(citaDetalles.getHoraInicio());
            cita.setHoraFin(citaDetalles.getHoraFin());
            cita.setEstado(citaDetalles.getEstado());

            if (citaDetalles.getServicio() != null && citaDetalles.getServicio().getId_servicio() != null) {
                Servicio servicio = servicioRepository.findById(citaDetalles.getServicio().getId_servicio())
                        .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
                cita.setServicio(servicio);
            }

            if (citaDetalles.getCliente() != null && citaDetalles.getCliente().getId() != null) {
                Cliente cliente = clienteRepository.findById(citaDetalles.getCliente().getId())
                        .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
                cita.setCliente(cliente);
            }

            if (citaDetalles.getAlumno() != null && citaDetalles.getAlumno().getId() != null) {
                Grupo alumno = grupoRepository.findById(citaDetalles.getAlumno().getId())
                        .orElseThrow(() -> new RuntimeException("Alumno/Grupo no encontrado"));
                cita.setAlumno(alumno);
            }

            return citaRepository.save(cita);
        });
    }

    @Override
    public boolean eliminarCita(Long id) {
        if (!citaRepository.existsById(id)) return false;
        citaRepository.deleteById(id);
        return true;
    }



    @Override
    public List<Cita> obtenerPorCliente(Cliente cliente) {
        return null;
    }

    @Override
    public List<Cita> obtenerPorAlumno(Grupo alumno) {
        return null;
    }

    @Override
    public List<Cita> obtenerPorServicio(Servicio servicio) {
        return null;
    }

    @Override
    public List<Cita> obtenerPorFecha(LocalDate fecha) {
        return null;
    }

    @Override
    public List<Cita> obtenerPorEstado(String estado) {
        return null;
    }

}
