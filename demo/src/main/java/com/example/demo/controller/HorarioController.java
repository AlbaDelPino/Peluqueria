package com.example.demo.controller;
import com.example.demo.domain.HorarioSemanal;
import com.example.demo.domain.Servicio;
import com.example.demo.exception.HorarioNotFoundException;
import com.example.demo.exception.ServicioNotFoundException;
import com.example.demo.security.service.HorarioService;
import com.example.demo.security.service.ServicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.controller.Response.NOT_FOUND;
@RestController
@RequestMapping("/horarios")
public class HorarioController {

    private final Logger logger = LoggerFactory.getLogger(HorarioController.class);

    @Autowired
    private HorarioService horarioService;

    // 🔹 Listar todos o filtrar por día/hora
    @Operation(summary = "Obtiene el listado de horarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de horarios",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = HorarioSemanal.class))))
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<HorarioSemanal>> getHorarios(
            @RequestParam(value = "diaSemana", required = false) String diaSemana,
            @RequestParam(value = "horaInicio", required = false) LocalTime horaInicio) {

        logger.info("inicio getHorarios");

        List<HorarioSemanal> horarios;
        if (diaSemana == null && horaInicio == null) {
            horarios = horarioService.findAll();
        } else if (diaSemana != null && horaInicio != null) {
            horarios = horarioService.findByDiaSemanaOrHoraInicio(diaSemana, horaInicio);
        } else if (diaSemana != null) {
            horarios = horarioService.findByDiaSemana(diaSemana);
        } else {
            horarios = horarioService.findByHoraInicio(horaInicio);
        }

        logger.info("fin getHorarios");
        return ResponseEntity.ok(horarios != null ? horarios : Collections.emptyList());
    }

    // 🔹 Obtener horario por ID
    @Operation(summary = "Obtiene un horario determinado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Existe el horario",
                    content = @Content(schema = @Schema(implementation = HorarioSemanal.class))),
            @ApiResponse(responseCode = "404", description = "El horario no existe",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<HorarioSemanal> getHorario(@PathVariable long id) {
        HorarioSemanal horario = horarioService.findById(id)
                .orElseThrow(() -> new HorarioNotFoundException(id));
        return new ResponseEntity<>(horario, HttpStatus.OK);
    }

    // 🔹 Crear horario
    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<HorarioSemanal> addHorario(@RequestBody HorarioSemanal horario) {
        HorarioSemanal addedHorario = horarioService.addHorario(horario);
        return new ResponseEntity<>(addedHorario, HttpStatus.CREATED);
    }

    // 🔹 Modificar horario
    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<HorarioSemanal> modifyHorario(@PathVariable long id,
                                                        @RequestBody HorarioSemanal newHorario) {
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

    // 🔹 Manejo de excepciones
    @ExceptionHandler(HorarioNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response> handleException(HorarioNotFoundException ex) {
        Response response = Response.errorResonse(NOT_FOUND, ex.getMessage());
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
