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
    public ResponseEntity<List<HorarioSemanal>> getHorarios() {
        return ResponseEntity.ok(horarioService.findAll());
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
    public ResponseEntity<List<HorarioSemanal>> getByDia(@RequestParam String diaSemana) {
        return ResponseEntity.ok(horarioService.findByDiaSemana(diaSemana.toUpperCase()));
    }

    // 🔹 Buscar por hora inicio
    @GetMapping("/horaInicio")
    public ResponseEntity<List<HorarioSemanal>> getByHoraInicio(@RequestParam LocalTime horaInicio) {
        return ResponseEntity.ok(horarioService.findByHoraInicio(horaInicio));
    }

    // 🔹 Buscar por servicio
    @GetMapping("/servicio/{id}")
    public ResponseEntity<List<HorarioSemanal>> getByServicio(@PathVariable Long id) {
        return ResponseEntity.ok(horarioService.findByServicio_IdServicio(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<HorarioSemanal>> getByDiaAndServicio(
            @RequestParam String diaSemana,
            @RequestParam Long idServicio
    ) {
        List<HorarioSemanal> horarios = horarioService.findByDiaSemanaAndServicio_IdServicio(diaSemana, idServicio);
        return ResponseEntity.ok(horarios);
    }


    // 🔹 Buscar por grupo
    @GetMapping("/grupo/{id}")
    public ResponseEntity<List<HorarioSemanal>> getByGrupo(@PathVariable Long idGrupo) {
        Grupo grupo = new Grupo();
        grupo.setId(idGrupo);
        return ResponseEntity.ok(horarioService.findByGrupo(grupo));
    }

    @PostMapping
    public ResponseEntity<?> addHorario(@RequestBody HorarioSemanal horario) {
        try {
            HorarioSemanal added = horarioService.addHorario(horario);
            return new ResponseEntity<>(added, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Creamos el mapa con la estructura que quieres
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());

            // Devolvemos el JSON con un código 400 (Bad Request)
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/importar")
    public ResponseEntity<Map<String,Integer>> importHorarios(@RequestBody List<HorarioSemanal> horarios) {
        Map<String,Integer> success = horarioService.importHorarios(horarios);
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
