package com.example.demo.repository;

import com.example.demo.domain.DiagnosticoEstetica;
import com.example.demo.domain.DiagnosticoPeluqueria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiagnosticoEsteticaRepository extends JpaRepository<DiagnosticoEstetica, Long> {
        Optional<DiagnosticoEstetica> findByClienteId(Long clienteId);
    }