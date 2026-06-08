package com.example.demo.security.service;

import com.example.demo.domain.CursoEscolar;
import com.example.demo.domain.HorarioSemanal;
import com.example.demo.exception.CursoNotFoundException;
import com.example.demo.repository.BloqueoRepository;
import com.example.demo.repository.CursoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private BloqueoRepository bloqueoRepository;

    @Override
    public List<CursoEscolar> findAll() {
        return cursoRepository.findAll();
    }


    @Override
    public Optional<CursoEscolar> findById(long id) {
        return cursoRepository.findById(id);
    }

    @Override
    public CursoEscolar findBySeleccionado(boolean selecionado) {
        return cursoRepository.findBySeleccionado(selecionado);
    }

    @Override
    public CursoEscolar addCurso(CursoEscolar curso) {
        return cursoRepository.save(curso);
    }

    @Override
    public CursoEscolar modifyCurso(long id, CursoEscolar newCurso) {
        cursoRepository.findById(id)
                .orElseThrow(() -> new CursoNotFoundException(id));
        return cursoRepository.save(newCurso);
    }

    @Override
    public CursoEscolar selecionarCurso(long id) {
        CursoEscolar cursoSeleccionado = cursoRepository.findById(id)
                .orElseThrow(() -> new CursoNotFoundException(id));

        List<CursoEscolar> cursos = cursoRepository.findAll();

        for (CursoEscolar curso : cursos) {
            if (curso.isSeleccionado()) {
                curso.setSeleccionado(false);
                cursoRepository.save(curso);
            }
        }

        cursoSeleccionado.setSeleccionado(true);
        return cursoRepository.save(cursoSeleccionado);
    }

    @Override
    public void deleteCurso(long id) {
        cursoRepository.deleteById(id);
    }
}
