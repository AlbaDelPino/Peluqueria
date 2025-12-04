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
    Optional<User> findByUsername(String username);
    List<User> findByRole(ERole role);
}
