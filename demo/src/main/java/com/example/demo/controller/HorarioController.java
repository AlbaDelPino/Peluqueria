package com.example.demo.controller;

import com.example.demo.domain.HorarioSemanal;
import com.example.demo.domain.Servicio;
import com.example.demo.domain.Grupo;
import com.example.demo.exception.HorarioNotFoundException;
import com.example.demo.security.service.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/horarios")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    // 🔹 Listar todos los horarios
    @GetMapping
    public ResponseEntity<List<HorarioSemanal>> getHorarios(@RequestParam String curso) {
        return ResponseEntity.ok(horarioService.findAll(curso));
    }

    // 🔹 Obtener horario por ID
    @GetMapping("/{id}")
    public ResponseEntity<HorarioSemanal> getHorario(@PathVariable long id) {
        HorarioSemanal horario = horarioService.findById(id)
                .orElseThrow(() -> new HorarioNotFoundException(id));
        return new ResponseEntity<>(horario, HttpStatus.OK);
    }

    // 🔹 Buscar por día de semana
    @GetMapping("/dia")
    public ResponseEntity<List<HorarioSemanal>> getByDia(@RequestParam String diaSemana,@RequestParam String curso) {
        return ResponseEntity.ok(horarioService.findByDiaSemanaAndCurso(diaSemana.toUpperCase(),curso.toUpperCase()));
    }

    // 🔹 Buscar por hora inicio
    @GetMapping("/horaInicio")
    public ResponseEntity<List<HorarioSemanal>> getByHoraInicio(@RequestParam LocalTime horaInicio,@RequestParam String curso) {
        return ResponseEntity.ok(horarioService.findByHoraInicioAndCurso(horaInicio,curso.toUpperCase()));
    }

    // 🔹 Buscar por servicio
    @GetMapping("/servicio/{id}")
    public ResponseEntity<List<HorarioSemanal>> getByServicio(@PathVariable Long id,@RequestParam String curso) {
        return ResponseEntity.ok(horarioService.findByServicioAndCurso(id,curso.toUpperCase()));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<HorarioSemanal>> getByDiaAndServicio(
            @RequestParam String diaSemana,
            @RequestParam Long idServicio,
            @RequestParam String curso
    ) {
        List<HorarioSemanal> horarios = horarioService.findByDiaSemanaAndServicioAndCurso(diaSemana, idServicio,curso.toUpperCase());
        return ResponseEntity.ok(horarios);
    }


    // 🔹 Buscar por grupo
    @GetMapping("/grupo/{id}")
    public ResponseEntity<List<HorarioSemanal>> getByGrupo(@PathVariable Long id,@RequestParam String curso) {
        Grupo grupo = new Grupo();
        grupo.setId(id);
        return ResponseEntity.ok(horarioService.findByGrupoAndCurso(grupo,curso.toUpperCase()));
    }

    // 🔹 Crear horario
    @PostMapping
    public ResponseEntity<HorarioSemanal> addHorario(@RequestBody HorarioSemanal horario) {
        HorarioSemanal added = horarioService.addHorario(horario);
        return new ResponseEntity<>(added, HttpStatus.CREATED);
    }

    // 🔹 Modificar horario
    @PutMapping("/{id}")
    public ResponseEntity<HorarioSemanal> modifyHorario(@PathVariable long id, @RequestBody HorarioSemanal newHorario) {
        HorarioSemanal horario = horarioService.modifyHorario(id, newHorario);
        return new ResponseEntity<>(horario, HttpStatus.OK);
    }

    // 🔹 Eliminar horario
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteHorario(@PathVariable long id) {
        horarioService.deleteHorario(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Horario eliminado correctamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
