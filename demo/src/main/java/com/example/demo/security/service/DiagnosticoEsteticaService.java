package com.example.demo.security.service;

import com.example.demo.domain.DiagnosticoEstetica;
import com.example.demo.domain.DiagnosticoPeluqueria;

import java.util.List;
import java.util.Optional;

public interface DiagnosticoEsteticaService {
    List<DiagnosticoEstetica> getAllDiagnosticos();
    Optional<DiagnosticoEstetica> getDiagnosticoByClienteId(Long clienteId);
    Optional<DiagnosticoEstetica> getDiagnosticoById(Long id);
    DiagnosticoEstetica saveOrUpdateDiagnostico(Long clienteId, DiagnosticoEstetica diagnosticoEstetica);
    void deleteDiagnostico(Long id);
}