package com.example.demo.controller;

import com.example.demo.domain.Cliente;
import com.example.demo.domain.FcmToken;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.FcmTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/fcm")
@CrossOrigin(origins = "*")
public class FcmController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FcmTokenRepository fcmTokenRepository;

    @PostMapping("/token/{clienteId}")
    public void registrarToken(@PathVariable Long clienteId, @RequestBody Map<String, String> body) {
        String tokenString = body.get("token");

        // 1. Verificamos si el token ya existe en la base de datos para no duplicar
        if (fcmTokenRepository.findByToken(tokenString).isPresent()) {
            System.out.println("ℹ️ El token ya está registrado.");
            return;
        }

        // 2. Si no existe, buscamos al cliente y lo asociamos
        clienteRepository.findById(clienteId).ifPresent(cliente -> {
            FcmToken nuevoToken = new FcmToken();
            nuevoToken.setToken(tokenString);
            nuevoToken.setCliente(cliente);

            fcmTokenRepository.save(nuevoToken);
            System.out.println("✅ Nuevo dispositivo registrado para: " + cliente.getNombre());
        });
    }
}