package com.example.demo.repository;

import com.example.demo.domain.Cliente;
import com.example.demo.domain.ERole;
import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByUsername(String username);

    // Para buscar por email (útil para validaciones de registro)
    Optional<Cliente> findByEmail(String email);

    // Verificar si ya existe un email antes de registrar
    Boolean existsByEmail(String email);
    Optional<User> findByTelefono(Long telefono);

    Boolean existsByUsername(String username);
}
