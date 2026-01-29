package com.example.demo.service.impl;

import com.example.demo.domain.Cliente;
import com.example.demo.domain.ComentarioCita;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.ComentarioCitaRepository;
import com.example.demo.service.ComentarioCitaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioCitaServiceImpl implements ComentarioCitaService {

    private final ComentarioCitaRepository comentarioRepo;
    private final ClienteRepository clienteRepo;

    public ComentarioCitaServiceImpl(ComentarioCitaRepository comentarioRepo, ClienteRepository clienteRepo) {
        this.comentarioRepo = comentarioRepo;
        this.clienteRepo = clienteRepo;
    }

    @Override
    public ComentarioCita crearComentario(Long clienteId, String comentario) {
        Cliente cliente = clienteRepo.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        ComentarioCita nuevo = new ComentarioCita(comentario, cliente);
        return comentarioRepo.save(nuevo);
    }

    @Override
    public ComentarioCita modificarComentario(Long comentarioId, String nuevoTexto) {
        ComentarioCita comentario = comentarioRepo.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        comentario.setComentario(nuevoTexto);
        return comentarioRepo.save(comentario);
    }

    @Override
    public void eliminarComentario(Long comentarioId) {
        if (!comentarioRepo.existsById(comentarioId)) {
            throw new RuntimeException("Comentario no encontrado");
        }
        comentarioRepo.deleteById(comentarioId);
    }

    @Override
    public List<ComentarioCita> listarComentariosPorCliente(Long clienteId) {
        return comentarioRepo.findByClienteId(clienteId);
    }
}
