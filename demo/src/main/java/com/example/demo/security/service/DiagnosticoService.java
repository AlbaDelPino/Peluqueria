package com.example.demo.security.service;

import com.example.demo.domain.Diagnostico;

import java.util.List;
import java.util.Optional;

public interface DiagnosticoService {
    List<Diagnostico> getAllDiagnosticos();
    Optional<Diagnostico> getDiagnosticoByClienteId(Long clienteId);
    Optional<Diagnostico> getDiagnosticoById(Long id);
    Diagnostico saveOrUpdateDiagnostico(Long clienteId, Diagnostico diagnostico);
    void deleteDiagnostico(Long id);
}
