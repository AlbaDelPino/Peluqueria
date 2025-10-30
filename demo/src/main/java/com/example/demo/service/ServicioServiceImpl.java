package com.example.demo.service;

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
    public Set<Servicio> findAll() {
        return servicioRepository.findAll();
    }

    @Override
    public Set<Servicio> findByNombre(String nombre) {
        return servicioRepository.findByNombre(nombre);
    }

    @Override
    public List<Servicio> findByNombreODescripcion(String nombre, String descripcion) {
        return servicioRepository.findByNombreODescripcion(nombre, descripcion);
    }

    @Override
    public List<Servicio> buscarPorNombreYDuracion(String nombre, long duracion) {
        return servicioRepository.findByNombreYDuracion(nombre, duracion);
    }

    @Override
    public List<Servicio> buscarPorNombreLikeYPrecio(String nombre, long precio) {
        return servicioRepository.findByNombreYPrecio(nombre, precio);
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

