package com.example.demo.controller;

import com.example.demo.domain.Cita;
import com.example.demo.exception.CitaNotFoundException;
import com.example.demo.security.service.CitaService;
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

import java.time.LocalDate;
import java.util.*;

import static com.example.demo.controller.Response.NOT_FOUND;

@RestController
@RequestMapping("/citas")
public class CitaController {

    private final Logger logger = LoggerFactory.getLogger(CitaController.class);

    @Autowired
    private CitaService citaService;

    // 🔹 Listar todas las citas o filtrar por fecha/estado
    @Operation(summary = "Obtiene el listado de citas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de citas",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Cita.class))))
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Cita>> getCitas(
            @RequestParam(value = "fecha", required = false) LocalDate fecha,
            @RequestParam(value = "estado", required = false) Boolean estado) {

        logger.info("inicio getCitas");

        List<Cita> citas;
        if (fecha != null && estado != null) {
            citas = citaService.findByFechaAndEstado(fecha, estado);
        } else if (fecha != null) {
            citas = citaService.findByFecha(fecha);
        } else if (estado != null) {
            citas = citaService.findByEstado(estado);
        } else {
            citas = citaService.findAll();
        }

        logger.info("fin getCitas");
        return ResponseEntity.ok(citas != null ? citas : Collections.emptyList());
    }

    // 🔹 Obtener cita por ID
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Cita> getCita(@PathVariable long id) {
        Cita cita = citaService.findById(id)
                .orElseThrow(() -> new CitaNotFoundException(id));
        return new ResponseEntity<>(cita, HttpStatus.OK);
    }

    // 🔹 Crear cita (resta plaza automáticamente)
    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Cita> addCita(@RequestBody Cita cita) {
        Cita addedCita = citaService.addCita(cita);
        return new ResponseEntity<>(addedCita, HttpStatus.CREATED);
    }

    // 🔹 Modificar cita
    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Cita> modifyCita(@PathVariable long id, @RequestBody Cita newCita) {
        Cita cita = citaService.modifyCita(id, newCita);
        return new ResponseEntity<>(cita, HttpStatus.OK);
    }

    // 🔹 Eliminar cita (suma plaza automáticamente)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCita(@PathVariable long id) {
        citaService.deleteCita(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cita eliminada correctamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 🔹 Cambiar estado de la cita (confirmada/cancelada)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Cita> cambiarEstado(@PathVariable long id, @RequestParam boolean estado) {
        Cita cita = citaService.cambiarEstado(id, estado);
        return new ResponseEntity<>(cita, HttpStatus.OK);
    }

    // 🔹 Manejo de excepciones
    @ExceptionHandler(CitaNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response> handleException(CitaNotFoundException ex) {
        Response response = Response.errorResonse(NOT_FOUND, ex.getMessage());
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
