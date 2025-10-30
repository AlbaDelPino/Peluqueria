package com.example.demo.service;

import com.example.demo.domain.Servicio;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface ServicioService {

    Set<Servicio> findAll();
    Set<Servicio> findByNombre(String nombre);



    Optional<Servicio> findById(long id);
    Servicio addServicio(Servicio servicio);
    Servicio modifyServicio(long id, Servicio newServicio);
    void deleteServicio(long id);
    List<Servicio> findByNombreOrDescripcion(String nombre, String descripcion);
    List<Servicio> buscarPorNombreAndDuracion(String nombre, long duracion);
    List<Servicio> buscarPorNombreAndPrecio(String nombre, long precio);
}
