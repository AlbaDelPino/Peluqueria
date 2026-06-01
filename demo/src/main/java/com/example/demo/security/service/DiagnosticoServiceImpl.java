package com.example.demo.security.service;

import com.example.demo.domain.Cliente;
import com.example.demo.domain.Diagnostico;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.DiagnosticoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiagnosticoServiceImpl implements DiagnosticoService {

    private final DiagnosticoRepository diagnosticoRepository;
    private final ClienteRepository clienteRepository;

    public DiagnosticoServiceImpl(DiagnosticoRepository diagnosticoRepository, ClienteRepository clienteRepository) {
        this.diagnosticoRepository = diagnosticoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<Diagnostico> getAllDiagnosticos() {
        return diagnosticoRepository.findAll();
    }

    @Override
    public Optional<Diagnostico> getDiagnosticoByClienteId(Long clienteId) {
        return diagnosticoRepository.findByClienteId(clienteId);
    }

    @Override
    public Optional<Diagnostico> getDiagnosticoById(Long id) {
        return diagnosticoRepository.findById(id);
    }

    @Override
    public Diagnostico saveOrUpdateDiagnostico(Long clienteId, Diagnostico diagnostico) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        diagnostico.setCliente(cliente);
        return diagnosticoRepository.save(diagnostico);
    }

    @Override
    public void deleteDiagnostico(Long id) {
        diagnosticoRepository.deleteById(id);
    }
}
