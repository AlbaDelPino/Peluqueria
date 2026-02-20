package com.example.demo.security.service;

import com.example.demo.domain.Cliente;
import com.example.demo.domain.ERole;
import com.example.demo.domain.User;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final UserRepository userRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public ClienteService(UserRepository userRepository,
                          ClienteRepository clienteRepository,
                          PasswordEncoder passwordEncoder,
                          JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    // Obtener todos los clientes
    public List<User> getAllClientes() {
        return userRepository.findByRole(ERole.ROLE_CLIENTE);
    }


    // Obtener cliente por ID
    public User getClienteById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.filter(u -> u.getRole() == ERole.ROLE_CLIENTE).orElse(null);
    }

    // Crear un cliente

    public Cliente createCliente(Cliente cliente) {
        // 1. Configuración básica
        cliente.setRole(ERole.ROLE_CLIENTE);
        // Empieza sin verificar

        // 2. Cifrar la contraseña
        if (cliente.getContrasenya() != null) {
            cliente.setContrasenya(passwordEncoder.encode(cliente.getContrasenya()));
        }

        // 3. Guardar en BD
        Cliente nuevoCliente = clienteRepository.save(cliente);

        // 4. Enviar Correo de Verificación


        return nuevoCliente;
    }




    // Obtener cliente por username
    public User getClienteByUsername(String username) {
        return userRepository.findByUsername(username)
                .filter(u -> u.getRole() == ERole.ROLE_CLIENTE)
                .orElse(null);
    }

    // Actualizar cliente por ID
    public User updateCliente(Long id, Cliente clienteDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) return null;

        User user = optionalUser.get();

        // 🔹 Atributos comunes (User)
        user.setNombre(clienteDetails.getNombre());
        user.setUsername(clienteDetails.getUsername());
        user.setEmail(clienteDetails.getEmail());


        if (clienteDetails.getContrasenya() != null && !clienteDetails.getContrasenya().isBlank()) {
            user.setContrasenya(passwordEncoder.encode(clienteDetails.getContrasenya()));
        }

        // 🔹 Atributos específicos (Cliente)
        if (user instanceof Cliente cliente) {

            cliente.setObservacion(clienteDetails.getObservacion());
            cliente.setTelefono(clienteDetails.getTelefono());
            cliente.setAlergenos(clienteDetails.getAlergenos());

            if (clienteDetails.getImagen() != null && clienteDetails.getImagen().length > 0) {
                cliente.setImagen(clienteDetails.getImagen()); // 👈 ahora sí se guarda la imagen
            }
        }

        return userRepository.save(user);
    }

    // Actualizar cliente por username
    public User updateClienteByUsername(String username, Cliente clienteDetails) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) return null;

        User user = optionalUser.get();

        // 🔹 Atributos comunes
        user.setNombre(clienteDetails.getNombre() != null ? clienteDetails.getNombre() : user.getNombre());
        user.setUsername(clienteDetails.getUsername() != null ? clienteDetails.getUsername() : user.getUsername());
        user.setEmail(clienteDetails.getEmail() != null ? clienteDetails.getEmail() : user.getEmail());

        if (clienteDetails.getContrasenya() != null && !clienteDetails.getContrasenya().isBlank()) {
            user.setContrasenya(passwordEncoder.encode(clienteDetails.getContrasenya()));
        }

        // 🔹 Atributos específicos de Cliente
        if (user instanceof Cliente cliente) {
            cliente.setObservacion(clienteDetails.getObservacion() != null ? clienteDetails.getObservacion() : cliente.getObservacion());
            cliente.setAlergenos(clienteDetails.getAlergenos() != null ? clienteDetails.getAlergenos() : cliente.getAlergenos());

            if (clienteDetails.getImagen() != null && clienteDetails.getImagen().length > 0) {
                cliente.setImagen(clienteDetails.getImagen()); // 👈 añadimos imagen
            }
        }

        return userRepository.save(user);
    }

    // Eliminar un cliente
    public boolean deleteCliente(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get();
        if (user.getRole() != ERole.ROLE_CLIENTE) return false;

        userRepository.deleteById(id);
        return true;
    }
    // En ClienteServiceImpl.java
    public void enviarCorreoRecuperacion(User user) {
        // 1. Generar código de 6 dígitos
        String codigo = String.valueOf((int)(Math.random() * 900000) + 100000);

        // 2. Verificar si es un Cliente y guardar el código
        if (user instanceof Cliente cliente) {
            cliente.setCodigoVerificacion(codigo); // Ahora sí reconoce el método
            userRepository.save(cliente);

            // 3. Enviar correo
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(cliente.getEmail());
            message.setSubject("Recuperar Contraseña - Bernat Experience");
            message.setText("Tu código de recuperación es: " + codigo);
            mailSender.send(message);

            System.out.println("✅ Código de recuperación enviado a: " + cliente.getEmail());
        } else {
            System.err.println("❌ El usuario no es un Cliente, no tiene campo de código.");
        }
    }

    public boolean validarCodigoRecuperacion(String email, String codigo) {
        // Buscamos el usuario por email
        User user = userRepository.findByEmail(email).orElse(null);

        // Verificamos que no sea nulo, que sea un Cliente y que el código coincida
        if (user instanceof Cliente cliente) {
            if (cliente.getCodigoVerificacion() != null) {
                return cliente.getCodigoVerificacion().equals(codigo);
            }
        }

        return false;
    }

}