package com.example.demo.controller;

import com.example.demo.domain.CursoEscolar;
import com.example.demo.domain.HorarioSemanal;
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
    public ResponseEntity<List<HorarioSemanal>> getHorarios(@RequestParam Long idCurso) {
        return ResponseEntity.ok(horarioService.findAll(idCurso));
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
    public ResponseEntity<List<HorarioSemanal>> getByDia(@RequestParam String diaSemana,@RequestParam Long idCurso) {
        return ResponseEntity.ok(horarioService.findByDiaSemanaAndCurso_IdCurso(diaSemana.toUpperCase(),idCurso));
    }

    // 🔹 Buscar por hora inicio
    @GetMapping("/horaInicio")
    public ResponseEntity<List<HorarioSemanal>> getByHoraInicio(@RequestParam LocalTime horaInicio,@RequestParam Long idCurso) {
        return ResponseEntity.ok(horarioService.findByHoraInicioAndCurso_IdCurso(horaInicio,idCurso));
    }

    // 🔹 Buscar por servicio
    @GetMapping("/servicio/{id}")
    public ResponseEntity<List<HorarioSemanal>> getByServicio(@PathVariable Long id,@RequestParam Long idCurso) {
        return ResponseEntity.ok(horarioService.findByServicio_IdServicioAndCurso_IdCurso(id,idCurso));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<HorarioSemanal>> getByDiaAndServicio(
            @RequestParam String diaSemana,
            @RequestParam Long idServicio,
            @RequestParam Long idCurso
    ) {
        List<HorarioSemanal> horarios = horarioService.findByDiaSemanaAndServicio_IdServicioAndCurso_IdCurso(diaSemana, idServicio,idCurso);
        return ResponseEntity.ok(horarios);
    }


    // 🔹 Buscar por grupo
    @GetMapping("/grupo/{id}")
    public ResponseEntity<List<HorarioSemanal>> getByGrupo(@PathVariable Long idGrupo,@RequestParam Long idCurso) {
        Grupo grupo = new Grupo();
        grupo.setId(idGrupo);
        CursoEscolar curso = new CursoEscolar();
        curso.setIdCurso(idCurso);
        return ResponseEntity.ok(horarioService.findByGrupoAndCurso(grupo,curso));
    }

    // 🔹 Crear horario
    @PostMapping
    public ResponseEntity<HorarioSemanal> addHorario(@RequestBody HorarioSemanal horario) {
        HorarioSemanal added = horarioService.addHorario(horario);
        return new ResponseEntity<>(added, HttpStatus.CREATED);
    }

    @PostMapping("/importar")
    public ResponseEntity<Boolean> importHorarios(@RequestBody List<HorarioSemanal> horarios) {
        boolean success = horarioService.importHorarios(horarios);
        return new ResponseEntity<>(success, HttpStatus.CREATED);
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
