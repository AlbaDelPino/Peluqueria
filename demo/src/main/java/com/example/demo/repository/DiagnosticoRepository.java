package com.example.demo.repository;

import com.example.demo.domain.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {
    Optional<Diagnostico> findByClienteId(Long clienteId);
}
