package com.example.demo.controller;

import com.example.demo.domain.ComentarioCita;
import com.example.demo.service.ComentarioCitaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioCitaController {

    private final ComentarioCitaService comentarioService;

    public ComentarioCitaController(ComentarioCitaService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @PostMapping("/cliente/{clienteId}")
    public ComentarioCita crearComentario(
            @PathVariable Long clienteId,
            @RequestBody String comentarioTexto
    ) {
        return comentarioService.crearComentario(clienteId, comentarioTexto);
    }

    @PutMapping("/{comentarioId}")
    public ComentarioCita modificarComentario(
            @PathVariable Long comentarioId,
            @RequestBody String nuevoTexto
    ) {
        return comentarioService.modificarComentario(comentarioId, nuevoTexto);
    }

    @DeleteMapping("/{comentarioId}")
    public void eliminarComentario(@PathVariable Long comentarioId) {
        comentarioService.eliminarComentario(comentarioId);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<ComentarioCita> listarComentarios(@PathVariable Long clienteId) {
        return comentarioService.listarComentariosPorCliente(clienteId);
    }
}
