package com.example.demo.security.service;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    public UserService(UserRepository userRepository,
                       JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User saveUser(User user) {
        // Nota: idealmente encriptar contrasenya con BCrypt antes de guardar
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }


    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setContrasenya(updatedUser.getContrasenya());

            user.setRole(updatedUser.getRole());
            return userRepository.save(user);
        });
    }


    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) return false;
        userRepository.deleteById(id);
        return true;
    }
    // En UserService
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }



    // En ClienteServiceImpl.java
    public void enviarCorreoRecuperacion(User user) {
        // 1. Generar código de 6 dígitos
        String codigo = String.valueOf((int)(Math.random() * 900000) + 100000);

        // 2. Verificar si es un Cliente y guardar el código

            user.setCodigoVerificacion(codigo); // Ahora sí reconoce el método
            userRepository.save(user);

            // 3. Enviar correo
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Recuperar Contraseña - Bernat Experience");
            message.setText("Tu código de recuperación es: " + codigo);
            mailSender.send(message);

            System.out.println("✅ Código de recuperación enviado a: " + user.getEmail());

    }

    public boolean validarCodigoRecuperacion(String email, String codigo) {
        // Buscamos el usuario por email
        User user = userRepository.findByEmail(email).orElse(null);

        // Verificamos que no sea nulo, que sea un Cliente y que el código coincida

            if (user.getCodigoVerificacion() != null) {
                return user.getCodigoVerificacion().equals(codigo);
            }
        

        return false;
    }


}
