package com.example.demo.controller;



import com.example.demo.domain.Cita;

import com.example.demo.domain.Cliente;
import com.example.demo.domain.Grupo;
import com.example.demo.domain.HorarioSemanal;
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
import java.util.*;

import static com.example.demo.controller.Response.NOT_FOUND;


@RestController
@RequestMapping("/citas")
public class CitaController {

    private final Logger logger =
            LoggerFactory.getLogger(CitaController.class);

    @Autowired
    private CitaService citaService;

    @Operation(summary = "Obtiene el listado de citas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de citas",
                    content = @Content(array = @ArraySchema(schema =
                    @Schema(implementation = Cita.class)))),
    })
    @GetMapping(value = "/cita", produces = "application/json")
    public ResponseEntity<List<Cita>> getCita(
            @RequestParam(value = "horario", defaultValue = "") HorarioSemanal horario,
            @RequestParam(value = "cliente", defaultValue = "") Cliente cliente,
            @RequestParam(value = "grupo", defaultValue = "") Grupo grupo) {

        logger.info("inicio getCita");

        List<Cita> citas;
        if (horario==null && grupo==null && cliente==null) {
            citas = citaService.findAll();
        } else {
            citas = citaService.findByHorarioOrGrupoOrCliente ( horario, grupo, cliente);
        }

        logger.info("fin getCita");
        return ResponseEntity.ok(citas != null ? citas : Collections.emptyList());
    }



    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Existe la cita", content = @Content(schema = @Schema(implementation =
                    Cita.class))),
            @ApiResponse(responseCode = "404", description = "La cita no existe", content = @Content(schema = @Schema(implementation =
                    Response.class)))
    })

    /*@GetMapping("/serviciobynombreandduracion")
    public ResponseEntity<List<Cita>> getCitaByNombreAndDuracion(
            @RequestParam(value = "nombre", defaultValue = "")String nombre,
            @RequestParam(value = "duracion", defaultValue = "") long duracion) {

        return ResponseEntity.ok(citaService.findByNombreAndDuracion(nombre, duracion));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Existe la cita", content = @Content(schema = @Schema(implementation =
                    Cita.class))),
            @ApiResponse(responseCode = "404", description = "La cita no existe", content = @Content(schema = @Schema(implementation =
                    Response.class)))
    })
    @GetMapping("/serviciobynombreandprecio")
    public ResponseEntity<List<Cita>> getCitaByNombreAndPrecio(
            @RequestParam(value = "nombre", defaultValue = "") String nombre,
            @RequestParam(value = "precio", defaultValue = "0") long precio) {

        return ResponseEntity.ok(citaService.findByNombreAndPrecio(nombre, precio));
    }



    @GetMapping("/serviciobynombreordescripcion")
    public ResponseEntity<List<Cita>> getCitaByNombreOrDescripcion(
            @RequestParam(value = "nombre", defaultValue = "")String nombre,
            @RequestParam(value = "descripcion", defaultValue = "") String descripcion) {

        return ResponseEntity.ok(citaService.findByNombreOrDescripcion(nombre, descripcion));

    }

    @Operation(summary = "Obtiene una cita determinado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Existe la cita", content = @Content(schema = @Schema(implementation =
                    Cita.class))),
            @ApiResponse(responseCode = "404", description = "La cita no existe", content = @Content(schema = @Schema(implementation =
                    Response.class)))
    })*/

    @GetMapping(value = "/cita/{id}", produces = "application/json")
    public ResponseEntity<Cita> getCita(@PathVariable long id) {
        Cita cita = citaService.findById(id)
                .orElseThrow(() -> new CitaNotFoundException(id));

        return new ResponseEntity<>(cita, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se registra la cita", content = @Content(schema = @Schema(implementation =
                    Cita.class)))
    })
    @PostMapping(value = "/cita", produces = "application/json",
            consumes = "application/json")
    public ResponseEntity<Cita> addCita(@RequestBody Cita cita) {
        Cita addedCita = citaService.addCita(cita);
        return new ResponseEntity<>(addedCita, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se modifica la cita", content = @Content(schema = @Schema(implementation =
                    Cita.class))),
            @ApiResponse(responseCode = "404", description = "La cita no existe", content = @Content(schema = @Schema(implementation =
                    Response.class)))
    })
    @PutMapping(value = "/cita/{id}", produces = "application/json",
            consumes = "application/json")
    public ResponseEntity<Cita> modifyCita(@PathVariable long id,
                                                   @RequestBody Cita newCita) {
        Cita cita = citaService.modifyCita(id, newCita);
        return new ResponseEntity<>(cita, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se elimina la cita", content = @Content(schema = @Schema(implementation =
                    Response.class))),
            @ApiResponse(responseCode = "404", description = "La cita no existe", content = @Content(schema = @Schema(implementation =
                    Response.class)))
    })
    @DeleteMapping("/cita/{id}")
    public ResponseEntity<Map<String, String>> deletev(@PathVariable long id) {
        citaService.deleteCita(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cita eliminada correctamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @ExceptionHandler(CitaNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response>
    handleException(CitaNotFoundException pnfe) {
        Response response = Response.errorResonse(NOT_FOUND,
                pnfe.getMessage());
        logger.error(pnfe.getMessage(), pnfe);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
