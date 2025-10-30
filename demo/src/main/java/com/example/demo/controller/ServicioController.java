package com.example.demo.controller;

import com.example.demo.domain.Servicio;
import com.example.demo.exception.ServicioNotFoundException;
import com.example.demo.service.ServicioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.demo.controller.Response.NOT_FOUND;

@RestController
public class ServicioController {

    private final Logger logger =
            LoggerFactory.getLogger(ServicioController.class);

    @Autowired
    private ServicioService servicioService;

    @Operation(summary = "Obtiene el listado de servicios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de servicios",
                    content = @Content(array = @ArraySchema(schema =
                    @Schema(implementation = Servicio.class)))),
    })

    @GetMapping(value = "/servicio", produces = "application/json")
    public ResponseEntity<Set<Servicio>> getServicios(@RequestParam(value =
            "nombre", defaultValue = "") String nombre, @RequestParam(value =
            "descripcion", defaultValue = "") String descripcion) {
        logger.info("inicio getServicios");
        Set<Servicio> servicios = null;
        if (nombre.equals("")&&descripcion.equals(""))
            servicios = servicioService.findAll();
        else
            servicios = (Set<Servicio>) servicioService.findByNombreOrDescripcion(nombre,descripcion);

        logger.info("fin getServicios");
        return new ResponseEntity<>(servicios, HttpStatus.OK);
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Existe el servicio", content = @Content(schema = @Schema(implementation =
                    Servicio.class))),
            @ApiResponse(responseCode = "404", description = "El servicio no existe", content = @Content(schema = @Schema(implementation =
                    Response.class)))
    })

    @GetMapping("/serviciobynombreandduracion")
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
            @RequestParam(value = "nombre", defaultValue = "")String nombre,
            @RequestParam(value = "precio", defaultValue = "") long precio) {

        return ResponseEntity.ok(servicioService.findByNombreAndPrecio(nombre, precio));
    }



    @GetMapping("/serviciobynombreordescripcion")
    public ResponseEntity<List<Servicio>> getServicioByNombreOrDescripcion(
        @RequestParam(value = "nombre", defaultValue = "")String nombre,
        @RequestParam(value = "descripcion", defaultValue = "") String descripcion) {

            return ResponseEntity.ok(servicioService.findByNombreOrDescripcion(nombre, descripcion));

    }

        @Operation(summary = "Obtiene un servicio determinado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Existe el servicio", content = @Content(schema = @Schema(implementation =
                    Servicio.class))),
            @ApiResponse(responseCode = "404", description = "El servicio no existe", content = @Content(schema = @Schema(implementation =
                    Response.class)))
    })

    @GetMapping(value = "/servicio/{id}", produces = "application/json")
    public ResponseEntity<Servicio> getServicio(@PathVariable long id) {
        Servicio servicio = servicioService.findById(id)
                .orElseThrow(() -> new ServicioNotFoundException(id));

        return new ResponseEntity<>(servicio, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se registra el servicio", content = @Content(schema = @Schema(implementation =
                    Servicio.class)))
    })
    @PostMapping(value = "/servicio", produces = "application/json",
            consumes = "application/json")
    public ResponseEntity<Servicio> addServicio(@RequestBody Servicio servicio) {
        Servicio addedServicio = servicioService.addServicio(servicio);
        return new ResponseEntity<>(addedServicio, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se modifica el servicio", content = @Content(schema = @Schema(implementation =
                    Servicio.class))),
            @ApiResponse(responseCode = "404", description = "El servicio no existe", content = @Content(schema = @Schema(implementation =
                    Response.class)))
    })
    @PutMapping(value = "/servicio/{id}", produces = "application/json",
            consumes = "application/json")
    public ResponseEntity<Servicio> modifyServicio(@PathVariable long id,
                                               @RequestBody Servicio newServicio) {
        Servicio servicio = servicioService.modifyServicio(id, newServicio);
        return new ResponseEntity<>(servicio, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se elimina el servicio", content = @Content(schema = @Schema(implementation =
                    Response.class))),
            @ApiResponse(responseCode = "404", description = "El servicio no existe", content = @Content(schema = @Schema(implementation =
                    Response.class)))
    })
    @DeleteMapping(value = "/servicio/{id}", produces =
            "application/json")
    public ResponseEntity<Response> deleteServicio(@PathVariable long id)
    {
        servicioService.deleteServicio(id);
        return new ResponseEntity<>(Response.noErrorResponse(),
                HttpStatus.OK);
    }

    @ExceptionHandler(ServicioNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response>
    handleException(ServicioNotFoundException pnfe) {
        Response response = Response.errorResonse(NOT_FOUND,
                pnfe.getMessage());
        logger.error(pnfe.getMessage(), pnfe);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
