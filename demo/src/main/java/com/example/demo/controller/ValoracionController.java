package com.example.demo.controller;



import com.example.demo.domain.Valoracion;
import com.example.demo.security.service.ValoracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/valoraciones")
public class ValoracionController {

    @Autowired
    private ValoracionService valoracionService;

    // ⭐ Crear valoración
    @PreAuthorize("hasAnyRole('CLIENTE')")
    @PostMapping
    public ResponseEntity<Valoracion> crearValoracion(@RequestBody Valoracion valoracion) {
        Valoracion nueva = valoracionService.crearValoracion(valoracion);
        return ResponseEntity.ok(nueva);
    }

    // ⭐ Obtener valoración por ID
    @GetMapping("/{id}")
    public ResponseEntity<Valoracion> obtenerValoracion(@PathVariable Long id) {
        Valoracion valoracion = valoracionService.findById(id);
        return ResponseEntity.ok(valoracion);
    }
    @GetMapping
    public ResponseEntity<List<Valoracion>> getAllValoraciones() {
        return ResponseEntity.ok(valoracionService.findAllValoraciones());
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Valoracion>> getValoracionesByCliente(@PathVariable Long idCliente) {
        return ResponseEntity.ok(valoracionService.findValoracionesByCliente(idCliente));
    }



    // ⭐ Eliminar valoración
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE')")
    public ResponseEntity<Void> eliminarValoracion(@PathVariable Long id) {
        valoracionService.deleteValoracion(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE')")
    public ResponseEntity<Valoracion> actualizarValoracion(
            @PathVariable Long id,
            @RequestBody Valoracion valoracion
    ) {
        Valoracion actualizada = valoracionService.actualizarValoracion(id, valoracion);
        return ResponseEntity.ok(actualizada);
    }

}
