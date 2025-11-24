package com.example.demo.security.service;

import com.example.demo.domain.Servicio;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface ServicioService {

    List<Servicio> findAll();
    Set<Servicio> findByNombre(String nombre);
    Set<Servicio> findByDescripcion(String descripcion);
    Optional<Servicio> findById(long id);
    List<Servicio> findByTipoId(long id_tipo_servicio);

    Servicio addServicio(Servicio servicio);
    Servicio modifyServicio(long id, Servicio newServicio);
    void deleteServicio(long id);
    void deleteAllByTipoId(long id);

    List<Servicio> findByNombreOrDescripcion(String nombre, String descripcion);
    List<Servicio> findByNombreAndDuracion(String nombre, long duracion);
    List<Servicio> findByNombreAndPrecio(String nombre, long precio);
}
