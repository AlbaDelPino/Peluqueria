package com.example.demo.security.service;

import com.example.demo.domain.DiagnosticoPeluqueria;

import java.util.List;
import java.util.Optional;

public interface DiagnosticoPeluqueriaService {
    List<DiagnosticoPeluqueria> getAllDiagnosticos();
    Optional<DiagnosticoPeluqueria> getDiagnosticoByClienteId(Long clienteId);
    Optional<DiagnosticoPeluqueria> getDiagnosticoById(Long id);
    DiagnosticoPeluqueria saveOrUpdateDiagnostico(Long clienteId, DiagnosticoPeluqueria diagnosticoPeluqueria);
    void deleteDiagnostico(Long id);
}
