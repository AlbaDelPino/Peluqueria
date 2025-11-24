package com.example.demo.controller;

import com.example.demo.domain.TipoServicio;
import com.example.demo.security.service.TipoServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tiposervicio")
public class TipoServicioController {

    @Autowired
    private TipoServicioService tipoServicioService;

    @GetMapping
    public ResponseEntity<List<TipoServicio>> getAll() {
        return ResponseEntity.ok(tipoServicioService.findAll());
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
