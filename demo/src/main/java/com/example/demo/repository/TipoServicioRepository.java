package com.example.demo.repository;

import com.example.demo.domain.TipoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TipoServicioRepository extends JpaRepository<TipoServicio, Long> {
    List<TipoServicio> findAll();
    Optional<TipoServicio> findByNombre(String nombre);
}
