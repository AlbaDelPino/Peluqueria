package com.example.demo.repository;

import com.example.demo.domain.FcmToken;
import com.example.demo.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByToken(String token);
    List<FcmToken> findByCliente(Cliente cliente);
}