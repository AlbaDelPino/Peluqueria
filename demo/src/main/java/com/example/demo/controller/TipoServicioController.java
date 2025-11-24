package com.example.demo.controller;

import com.example.demo.domain.Servicio;
import com.example.demo.domain.TipoServicio;
import com.example.demo.exception.TipoServicioNotFoundException;
import com.example.demo.security.service.ServicioService;
import com.example.demo.security.service.TipoServicioService;
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

import java.util.List;


@RestController
public class TipoServicioController {

    private final Logger logger =
            LoggerFactory.getLogger(ServicioController.class);

    @Autowired
    private TipoServicioService tipoServicioService;

    @Operation(summary = "Obtiene el listado de tipos de servicio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de tipos de servicio",
                    content = @Content(array = @ArraySchema(schema =
                    @Schema(implementation = Servicio.class)))),
    })
    @GetMapping(value = "/tiposervicio", produces = "application/json")
    public ResponseEntity<List<TipoServicio>> getTiposServicio() {
        return ResponseEntity.ok(tipoServicioService.findAll());
    }

    @GetMapping(value = "/tiposervicio/{id}", produces = "application/json")
    public ResponseEntity<TipoServicio> getTipoServicio(@PathVariable long id) {
        TipoServicio tipo = tipoServicioService.findById(id)
                .orElseThrow(() -> new TipoServicioNotFoundException(id));

        return new ResponseEntity<>(tipo, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TipoServicio> add(@RequestBody TipoServicio tipoServicio) {
        return ResponseEntity.ok(tipoServicioService.addTipoServicio(tipoServicio));
    }

    @PutMapping("/{id}")
    public TipoServicio upadateTipoServicio(@PathVariable Long id, @RequestBody TipoServicio tipoServicio) {
        return tipoServicioService.upadateTipoServicio(id, tipoServicio);
    }


    @DeleteMapping("/{id}")
    public void deleteTipoServicio(@PathVariable Long id) {
        tipoServicioService.deleteTipoServicio(id);

    }
}
