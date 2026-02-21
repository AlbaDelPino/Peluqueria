package com.example.demo.security.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

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