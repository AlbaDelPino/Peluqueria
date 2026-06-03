package com.example.demo.security.service;

import com.example.demo.domain.CursoEscolar;
import com.example.demo.domain.Grupo;
import com.example.demo.domain.HorarioSemanal;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<CursoEscolar> findAll();
    Optional<CursoEscolar> findById(long id);

    CursoEscolar findBySeleccionado(boolean selecionado);

    CursoEscolar addCurso(CursoEscolar curso);
    CursoEscolar modifyCurso(long id, CursoEscolar newCurso);
    CursoEscolar selecionarCurso(long id);
    void deleteCurso(long id);

}
