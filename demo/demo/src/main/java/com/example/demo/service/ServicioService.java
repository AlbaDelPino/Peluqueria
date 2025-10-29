package com.example.demo.service;

import com.example.demo.domain.Servicio;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface ServicioService {

    Set<Servicio> findAll();

    Set<Servicio> findByNombre(String nombre);



    Optional<Servicio> findById(long id);



    Servicio addAlumno(Servicio servicio);

    Servicio modifyAlumno(long id, Servicio newServicio);

    void deleteAlumno(long id);

    List<Servicio> buscarPorNombreODescripcion(String nombre, String descripcion);

    List<Servicio> buscarPorNombreYDuracion(String nombre, long duracion);

    List<Servicio> buscarPorNombreLikeYPrecio(String nombre, long precio);
}
