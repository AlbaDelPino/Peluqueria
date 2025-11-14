package com.example.demo.security.service;

import com.example.demo.exception.ServicioNotFoundException;
import com.example.demo.repository.ServicioRepository;
import com.example.demo.domain.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ServicioServiceImpl implements ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    @Override
    public List<Servicio> findAll() {
        return servicioRepository.findAll();
    }

    @Override
    public Set<Servicio> findByNombre(String nombre) {
        return servicioRepository.findByNombre(nombre);
    }

    @Override
    public Set<Servicio> findByDescripcion(String descripcion) { return servicioRepository.findByDescripcion(descripcion); }

    @Override
    public List<Servicio> findByNombreOrDescripcion(String nombre, String descripcion) {
        return servicioRepository.findByNombreOrDescripcion(nombre, descripcion);
    }

    @Override
    public List<Servicio> findByNombreAndDuracion(String nombre, long duracion) {
        return servicioRepository.findByNombreAndDuracion(nombre, duracion);
    }

    @Override
    public List<Servicio> findByNombreAndPrecio(String nombre, long precio) {
        return servicioRepository.findByNombreAndPrecio(nombre, precio);
    }

    @Override
    public Optional<Servicio> findById(long id) {
        return servicioRepository.findById(id);
    }

    @Override
    public Servicio addServicio(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    @Override
    public Servicio modifyServicio(long id, Servicio newServicio) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ServicioNotFoundException(id));
        newServicio.setId_servicio(servicio.getId_servicio());
        return servicioRepository.save(newServicio);
    }

    @Override
    public void deleteServicio(long id) {
        servicioRepository.findById(id)
                .orElseThrow(() -> new ServicioNotFoundException(id));
        servicioRepository.deleteById(id);
    }

}

