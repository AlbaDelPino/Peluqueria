package com.example.demo.controller;

import com.example.demo.domain.DiagnosticoEstetica;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.security.service.DiagnosticoEsteticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/diagnosticos/estetica")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DiagnosticoEsteticaController {

    @Autowired
    private DiagnosticoEsteticaService diagnosticoEsteticaService;

    // --- LIST ALL ---
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','GRUPO','CLIENTE')")
    public List<DiagnosticoEstetica> getAll() {
        return diagnosticoEsteticaService.getAllDiagnosticos();
    }

    // --- GET BY CLIENTE ID ---
    // --- GET BY CLIENTE ID ---
    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('ADMIN','GRUPO','CLIENTE')")
    public ResponseEntity<?> getByClienteId(@PathVariable Long clienteId) {
        Optional<DiagnosticoEstetica> opt = diagnosticoEsteticaService.getDiagnosticoByClienteId(clienteId);

        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Diagnóstico no encontrado para el cliente"));
        }
    }

    // --- CREATE OR UPDATE FOR CLIENTE ---
    @PostMapping("/cliente/{clienteId}")

    public ResponseEntity<?> saveOrUpdate(@PathVariable Long clienteId, @RequestBody DiagnosticoEstetica diagnosticoEstetica) {
        DiagnosticoEstetica saved = diagnosticoEsteticaService.saveOrUpdateDiagnostico(clienteId, diagnosticoEstetica);
        return ResponseEntity.ok(new MessageResponse("Diagnóstico guardado con ID " + saved.getId()));
    }

    // --- DELETE BY ID ---
    @DeleteMapping("/{id}")

    public ResponseEntity<?> delete(@PathVariable Long id) {
        diagnosticoEsteticaService.deleteDiagnostico(id);
        return ResponseEntity.ok(new MessageResponse("Diagnóstico eliminado"));
    }
}
