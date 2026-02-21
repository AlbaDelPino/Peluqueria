package com.example.demo.controller;

import com.example.demo.security.service.FCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/test-fcm")
public class NotificationTestController {

    @Autowired
    private FCMService fcmService;

    // Ahora este método sí funciona de verdad
    @GetMapping("/cliente/{id}")
    public String pruebaCliente(@PathVariable Long id) {
        fcmService.enviarANombreDeCliente(id, "¡Hola desde la BD! 💈", "Tu servidor ha encontrado tu dirección y te ha enviado este mensaje.");
        return "✅ He buscado en la base de datos y enviado la notificación al cliente " + id;
    }
}