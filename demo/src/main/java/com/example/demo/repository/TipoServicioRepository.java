package com.example.demo.repository;

import com.example.demo.domain.TipoServicio;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TipoServicioRepository extends CrudRepository<TipoServicio, Long> {
    List<TipoServicio> findAll();
    TipoServicio findByNombre(String nombre);
}
