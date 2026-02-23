package com.example.demo.repository;

import com.example.demo.domain.ComentarioCita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioCitaRepository extends JpaRepository<ComentarioCita, Long> {

    List<ComentarioCita> findByClienteId(Long clienteId);
}
