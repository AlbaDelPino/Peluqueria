package com.example.demo.security.service;

import com.example.demo.domain.Cita;
import com.example.demo.domain.EstadoCita;
import com.example.demo.domain.Valoracion;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.ValoracionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValoracionServiceImpl implements ValoracionService {

    @Autowired
    private ValoracionRepository valoracionRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Override
    public Valoracion crearValoracion(Valoracion valoracion, Long idCliente) {

        // 1. Validar puntuación
        if (valoracion.getPuntuacion() < 0 || valoracion.getPuntuacion() > 5) {
            throw new RuntimeException("La puntuación debe estar entre 0 y 5.");
        }
        if (valoracion.getTrato() < 0 || valoracion.getTrato() > 5) {
            throw new RuntimeException("El trato debe estar entre 0 y 5.");
        }
        if (valoracion.getDesarrollo() < 0 || valoracion.getDesarrollo() > 5) {
            throw new RuntimeException("El desarrollo debe estar entre 0 y 5.");
        }
        if (valoracion.getComunicacion() < 0 || valoracion.getComunicacion() > 5) {
            throw new RuntimeException("La comunicacion debe estar entre 0 y 5.");
        }
        if (valoracion.getOrganizacion() < 0 || valoracion.getOrganizacion() > 5) {
            throw new RuntimeException("La organizacion debe estar entre 0 y 5.");
        }

        // 2. Validar que la cita existe
        if (valoracion.getCita() == null || valoracion.getCita().getId() == null) {
            throw new RuntimeException("El objeto cita o su ID no han sido enviados correctamente.");
        }

        Long idCita = valoracion.getCita().getId();
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("La cita con ID " + idCita + " no existe."));
        if (cita.getEstado() != EstadoCita.COMPLETADO){
            throw new RuntimeException("La cita no está completada.");
        }
        if (idCliente!= cita.getCliente().getId()){
            throw new RuntimeException("Esta cita no es de este cliente.");
        }

        // 3. Validar que la cita NO tenga ya una valoración
        if (valoracionRepository.existsByCita_Id(idCita)) {
            throw new RuntimeException("Esta cita ya tiene una valoración.");
        }


        // 4. Asignar la cita real
        valoracion.setCita(cita);

        // 5. Guardar
        return valoracionRepository.save(valoracion);
    }

    @Override
    public Valoracion findById(Long id) {
        return valoracionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Valoración no encontrada"));
    }

    @Override
    public void deleteValoracion(Long id) {
        if (!valoracionRepository.existsById(id)) {
            throw new RuntimeException("Valoración no encontrada");
        }
        valoracionRepository.deleteById(id);
    }
    @Override
    public List<Valoracion> findAllValoraciones() {
        return valoracionRepository.findAll();
    }

    @Override
    public List<Valoracion> findValoracionesByCliente(Long clienteId) {
        return valoracionRepository.findByCita_Cliente_Id(clienteId);
    }

    @Override
    public Valoracion actualizarValoracion(Long id, Valoracion valoracionActualizada, Long idCliente) {

        // 1. Buscar la valoración existente
        Valoracion valoracion = valoracionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Valoración no encontrada"));

        // 2. Validar puntuación
        if (valoracionActualizada.getPuntuacion() < 0 || valoracionActualizada.getPuntuacion() > 5) {
            throw new RuntimeException("La puntuación debe estar entre 0 y 5.");
        }
        if (valoracionActualizada.getTrato() < 0 || valoracionActualizada.getTrato() > 5) {
            throw new RuntimeException("El trato debe estar entre 0 y 5.");
        }
        if (valoracionActualizada.getDesarrollo() < 0 || valoracionActualizada.getDesarrollo() > 5) {
            throw new RuntimeException("El desarrollo debe estar entre 0 y 5.");
        }
        if (valoracionActualizada.getComunicacion() < 0 || valoracionActualizada.getComunicacion() > 5) {
            throw new RuntimeException("La comunicacion debe estar entre 0 y 5.");
        }
        if (valoracionActualizada.getOrganizacion() < 0 || valoracionActualizada.getOrganizacion() > 5) {
            throw new RuntimeException("La organizacion debe estar entre 0 y 5.");
        }

        Long idCita = valoracion.getCita().getId();
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("La cita no existe."));
        if (idCliente != cita.getCliente().getId()){
            throw new RuntimeException("Esta cita no es de este cliente.");
        }

        // 3. Actualizar campos permitidos
        valoracion.setComentario(valoracionActualizada.getComentario());
        valoracion.setPuntuacion(valoracionActualizada.getPuntuacion());
        valoracion.setTrato(valoracionActualizada.getTrato());
        valoracion.setDesarrollo(valoracionActualizada.getDesarrollo());
        valoracion.setComunicacion(valoracionActualizada.getComunicacion());
        valoracion.setOrganizacion(valoracionActualizada.getOrganizacion());
        valoracion.setCita(valoracionActualizada.getCita());
        valoracion.setImagen(valoracionActualizada.getImagen());

        // 4. Guardar cambios
        return valoracionRepository.save(valoracion);
    }


}
