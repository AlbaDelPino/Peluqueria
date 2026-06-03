package com.example.demo.controller;

import com.example.demo.domain.CursoEscolar;
import com.example.demo.domain.HorarioSemanal;
import com.example.demo.exception.CursoNotFoundException;
import com.example.demo.exception.HorarioNotFoundException;
import com.example.demo.security.service.CitaService;
import com.example.demo.security.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/curso")
@CrossOrigin(origins = "*")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<CursoEscolar>> getCursos() {
        return ResponseEntity.ok(cursoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoEscolar> getCurso(@PathVariable long id) {
        CursoEscolar curso = cursoService.findById(id)
                .orElseThrow(() -> new CursoNotFoundException(id));
        return new ResponseEntity<>(curso, HttpStatus.OK);
    }

    @GetMapping("/selecionado")
    public ResponseEntity<CursoEscolar> getBySeleccionado() {
        return ResponseEntity.ok(cursoService.findBySeleccionado(true));
    }

    @PostMapping
    public ResponseEntity<CursoEscolar> addCurso(@RequestBody CursoEscolar curso) {
        CursoEscolar added = cursoService.addCurso(curso);
        return new ResponseEntity<>(added, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoEscolar> modifyCurso(@PathVariable long id, @RequestBody CursoEscolar newCurso) {
        CursoEscolar curso = cursoService.modifyCurso(id, newCurso);
        return new ResponseEntity<>(curso, HttpStatus.OK);
    }

    @PutMapping("/selecionar/{id}")
    public ResponseEntity<CursoEscolar> selecionarCurso(@PathVariable long id) {
        CursoEscolar curso = cursoService.selecionarCurso(id);
        return new ResponseEntity<>(curso, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCurso(@PathVariable long id) {
        cursoService.deleteCurso(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Curso eliminado correctamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}