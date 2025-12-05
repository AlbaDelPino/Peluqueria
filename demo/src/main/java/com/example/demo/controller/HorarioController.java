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
public class HorarioController {

    private final Logger logger =
            LoggerFactory.getLogger(HorarioController.class);

    @Autowired
    private HorarioService horarioService;

    @Operation(summary = "Obtiene el listado de horarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de horarios",
                    content = @Content(array = @ArraySchema(schema =
                    @Schema(implementation = HorarioSemanal.class)))),
    })
    @GetMapping(value = "/horario", produces = "application/json")
    public ResponseEntity<List<HorarioSemanal>> getHorarios(
            @RequestParam(value = "diaSemana", defaultValue = "") String diaSemana,
            @RequestParam(value = "horaInicio", defaultValue = "") LocalTime horaInicio) {

        logger.info("inicio getHorarios");

        List<HorarioSemanal> horarios;
        if (diaSemana.isEmpty() && horaInicio.equals("")) {
            horarios = horarioService.findAll();
        } else {
            horarios = horarioService.findByDiaSemanaOrHoraInicio(diaSemana, horaInicio);
        }
        logger.info("fin getHorarios");
        return ResponseEntity.ok(horarios != null ? horarios : Collections.emptyList());
    }



    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Existe el horario", content = @Content(schema = @Schema(implementation =
                    Servicio.class))),
            @ApiResponse(responseCode = "404", description = "El horario no existe", content = @Content(schema = @Schema(implementation =
                    Response.class)))
    })

    /*@GetMapping("/serviciobynombreandduracion")
    public ResponseEntity<List<Servicio>> getServicioByNombreAndDuracion(
            @RequestParam(value = "nombre", defaultValue = "")String nombre,
            @RequestParam(value = "duracion", defaultValue = "") long duracion) {

        return ResponseEntity.ok(servicioService.findByNombreAndDuracion(nombre, duracion));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Existe el servicio", content = @Content(schema = @Schema(implementation =
                    Servicio.class))),
            @ApiResponse(responseCode = "404", description = "El servicio no existe", content = @Content(schema = @Schema(implementation =
                    Response.class)))
    })
    @GetMapping("/serviciobynombreandprecio")
    public ResponseEntity<List<Servicio>> getServicioByNombreAndPrecio(
            @RequestParam(value = "nombre", defaultValue = "") String nombre,
            @RequestParam(value = "precio", defaultValue = "0") long precio) {

        return ResponseEntity.ok(servicioService.findByNombreAndPrecio(nombre, precio));
    }*/



    @GetMapping("/horariobydiasemanaorhorainicio")
    public ResponseEntity<List<HorarioSemanal>> getServicioByNombreOrDescripcion(
            @RequestParam(value = "diaSemana", defaultValue = "")String diaSemana,
            @RequestParam(value = "horaInicio", defaultValue = "") LocalTime horaInicio) {

        return ResponseEntity.ok(horarioService.findByDiaSemanaOrHoraInicio(diaSemana, horaInicio));

    }

    @Operation(summary = "Obtiene un horario determinado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Existe el horario", content = @Content(schema = @Schema(implementation =
                    Servicio.class))),
            @ApiResponse(responseCode = "404", description = "El horario no existe", content = @Content(schema = @Schema(implementation =
                    Response.class)))
    })

    @GetMapping(value = "/horario/{id}", produces = "application/json")
    public ResponseEntity<HorarioSemanal> getServicio(@PathVariable long id) {
        HorarioSemanal horario = horarioService.findById(id)
                .orElseThrow(() -> new HorarioNotFoundException(id));

        return new ResponseEntity<>(horario, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se registra el horario", content = @Content(schema = @Schema(implementation =
                    Servicio.class)))
    })
    @PostMapping(value = "/horario", produces = "application/json",
            consumes = "application/json")
    public ResponseEntity<HorarioSemanal> addHorario(@RequestBody HorarioSemanal horario) {
        HorarioSemanal addedHorario = horarioService.addHorario(horario);
        return new ResponseEntity<>(addedHorario, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se modifica el horario", content = @Content(schema = @Schema(implementation =
                    Servicio.class))),
            @ApiResponse(responseCode = "404", description = "El horario no existe", content = @Content(schema = @Schema(implementation =
                    Response.class)))
    })
    @PutMapping(value = "/horario/{id}", produces = "application/json",
            consumes = "application/json")
    public ResponseEntity<HorarioSemanal> modifyHorario(@PathVariable long id,
                                                   @RequestBody HorarioSemanal newHorario) {
        HorarioSemanal horario = horarioService.modifyHorario(id, newHorario);
        return new ResponseEntity<>(horario, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se elimina el horario", content = @Content(schema = @Schema(implementation =
                    Response.class))),
            @ApiResponse(responseCode = "404", description = "El horario no existe", content = @Content(schema = @Schema(implementation =
                    Response.class)))
    })
    @DeleteMapping("/horario/{id}")
    public ResponseEntity<Map<String, String>> deleteHorario(@PathVariable long id) {
        horarioService.deleteHorario(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Horario eliminado correctamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @ExceptionHandler(HorarioNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response>
    handleException(HorarioNotFoundException pnfe) {
        Response response = Response.errorResonse(NOT_FOUND,
                pnfe.getMessage());
        logger.error(pnfe.getMessage(), pnfe);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
