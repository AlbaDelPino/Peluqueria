package com.example.demo.repository;

import com.example.demo.domain.DiagnosticoPeluqueria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DiagnosticoPeluqueriaRepository extends JpaRepository<DiagnosticoPeluqueria, Long> {
    Optional<DiagnosticoPeluqueria> findByClienteId(Long clienteId);
}
