package com.example.demo.controller;

import com.example.demo.domain.Diagnostico;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.security.service.DiagnosticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/diagnosticos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DiagnosticoController {

    @Autowired
    private DiagnosticoService diagnosticoService;

    // --- LIST ALL ---
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','GRUPO','CLIENTE')")
    public List<Diagnostico> getAll() {
        return diagnosticoService.getAllDiagnosticos();
    }

    // --- GET BY CLIENTE ID ---
    // --- GET BY CLIENTE ID ---
    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('ADMIN','GRUPO','CLIENTE')")
    public ResponseEntity<?> getByClienteId(@PathVariable Long clienteId) {
        Optional<Diagnostico> opt = diagnosticoService.getDiagnosticoByClienteId(clienteId);

        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Diagnóstico no encontrado para el cliente"));
        }
    }

    // --- CREATE OR UPDATE FOR CLIENTE ---
    @PostMapping("/cliente/{clienteId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveOrUpdate(@PathVariable Long clienteId, @RequestBody Diagnostico diagnostico) {
        Diagnostico saved = diagnosticoService.saveOrUpdateDiagnostico(clienteId, diagnostico);
        return ResponseEntity.ok(new MessageResponse("Diagnóstico guardado con ID " + saved.getId()));
    }

    // --- DELETE BY ID ---
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        diagnosticoService.deleteDiagnostico(id);
        return ResponseEntity.ok(new MessageResponse("Diagnóstico eliminado"));
    }
}
