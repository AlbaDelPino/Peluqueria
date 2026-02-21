package com.example.demo.security.service;

import com.example.demo.domain.FcmToken;
import com.example.demo.repository.FcmTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class FCMService {

    @Autowired
    private FcmTokenRepository fcmTokenRepository;

    // --- NUEVO MÉTODO: BUSCA Y ENVÍA ---
    public void enviarANombreDeCliente(Long clienteId, String titulo, String cuerpo) {
        // Buscamos el último token registrado para ese cliente
        Optional<FcmToken> fcmTokenOpt = fcmTokenRepository.findTopByClienteIdOrderByIdDesc(clienteId);

        if (fcmTokenOpt.isPresent()) {
            String token = fcmTokenOpt.get().getToken();
            enviarNotificacion(token, titulo, cuerpo);
        } else {
            System.err.println("❌ No hay token en la BD para el cliente: " + clienteId);
        }
    }

    // El método que ya tenías
    public void enviarNotificacion(String token, String titulo, String cuerpo) {
        try {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(titulo)
                            .setBody(cuerpo)
                            .build())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("🚀 Notificación enviada a Firebase: " + response);
        } catch (Exception e) {
            System.err.println("❌ Error al enviar a FCM: " + e.getMessage());
        }
    }
}