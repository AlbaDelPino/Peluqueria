package com.example.demo.repository;

import com.example.demo.domain.CursoEscolar;
import com.example.demo.domain.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CursoRepository extends JpaRepository<CursoEscolar, Long> {
    List<CursoEscolar> findAll();
    Optional<CursoEscolar> findById(long id);
    CursoEscolar findBySeleccionado(boolean seleccionado);
    Optional<CursoEscolar> findByNombre(String nombre);
}
