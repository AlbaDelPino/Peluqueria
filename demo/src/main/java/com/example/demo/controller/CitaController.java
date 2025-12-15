package com.example.demo.controller;

import com.example.demo.domain.Cita;
import com.example.demo.domain.Cliente;
import com.example.demo.domain.EstadoCita;
import com.example.demo.exception.CitaNotFoundException;
import com.example.demo.security.service.CitaService;
import com.example.demo.payload.request.CitaRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    // 🔹 Listar citas con filtros
    @GetMapping
    public ResponseEntity<List<Cita>> getCitas(
            @RequestParam(value = "fecha", required = false) LocalDate fecha,
            @RequestParam(value = "hora", required = false) LocalTime hora,
            @RequestParam(value = "estado", required = false) EstadoCita estado,
            @RequestParam(value = "cliente", required = false) Long clienteId) {

        List<Cita> citas;

        if (fecha != null && hora != null) {
            citas = citaService.findByFechaAndHora(fecha, hora);
        } else if (fecha != null) {
            citas = citaService.findByFecha(fecha);
        } else if (hora != null) {
            citas = citaService.findByHora(hora);
        } else if (estado != null) {
            citas = citaService.findByEstado(estado);
        } else if (clienteId != null) {
            Cliente cliente = new Cliente();
            cliente.setId(clienteId);
            citas = citaService.findByCliente(cliente);
        } else {
            citas = citaService.findAll();
        }

        return ResponseEntity.ok(citas);
    }

    // 🔹 Obtener cita por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cita> getCita(@PathVariable long id) {
        Cita cita = citaService.findById(id)
                .orElseThrow(() -> new CitaNotFoundException(id));
        return new ResponseEntity<>(cita, HttpStatus.OK);
    }

    // 🔹 Crear cita
    @PostMapping
    public ResponseEntity<Cita> addCita(@RequestBody CitaRequest request) {
        Cita cita = new Cita();
        cita.setFecha(request.getFecha());
        cita.setHora(request.getHora());
        cita.setEstado(EstadoCita.PENDIENTE); // por defecto

        Cliente cliente = new Cliente();
        cliente.setId(request.getClienteId());
        cita.setCliente(cliente);

        Cita addedCita = citaService.addCita(cita, request.getServicioId());
        return new ResponseEntity<>(addedCita, HttpStatus.CREATED);
    }



    // 🔹 Eliminar cita
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCita(@PathVariable long id) {
        citaService.deleteCita(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cita eliminada correctamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 🔹 Cambiar estado (confirmar/cancelar)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Cita> cambiarEstado(@PathVariable long id, @RequestParam EstadoCita estado) {
        Cita cita = citaService.cambiarEstado(id, estado);
        return new ResponseEntity<>(cita, HttpStatus.OK);
    }
}
