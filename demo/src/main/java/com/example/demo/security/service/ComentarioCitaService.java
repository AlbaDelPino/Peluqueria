package com.example.demo.service;

import com.example.demo.domain.ComentarioCita;

import java.util.List;

public interface ComentarioCitaService {

    ComentarioCita crearComentario(Long clienteId, String comentario);

    ComentarioCita modificarComentario(Long comentarioId, String nuevoTexto);

    void eliminarComentario(Long comentarioId);

    List<ComentarioCita> listarComentariosPorCliente(Long clienteId);
}
