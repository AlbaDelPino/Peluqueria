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
        cliente.setVerificado(false); // Empieza sin verificar

        // 2. Cifrar la contraseña
        if (cliente.getContrasenya() != null) {
            cliente.setContrasenya(passwordEncoder.encode(cliente.getContrasenya()));
        }

        // 3. Guardar en BD
        Cliente nuevoCliente = clienteRepository.save(cliente);

        // 4. Enviar Correo de Verificación
        try {
            enviarCorreoVerificacion(nuevoCliente);
        } catch (Exception e) {
            System.err.println("Error enviando correo: " + e.getMessage());
        }

        return nuevoCliente;
    }
    private void enviarCorreoVerificacion(Cliente cliente) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(cliente.getEmail());
            helper.setSubject("Activa tu cuenta - Bernat Experience");

            String urlVerificacion = "http://192.168.7.13:8082/clientes/verificar?id=" + cliente.getId();

            String htmlContent = "<h3>¡Hola " + cliente.getNombre() + "!</h3>" +
                    "<p>Pulsa el enlace para activar tu cuenta:</p>" +
                    "<a href='" + urlVerificacion + "'>ACTIVAR CUENTA</a>";

            helper.setText(htmlContent, true);

            mailSender.send(message);
            System.out.println("✅ CORREO ENVIADO CON ÉXITO A: " + cliente.getEmail());

        } catch (Exception e) {
            System.err.println("❌ ERROR AL ENVIAR CORREO: " + e.getMessage());
            // PLAN B: Imprimimos el link en consola por si el mail falla
            System.out.println("⚠️ USA ESTE LINK MANUALMENTE PARA VERIFICAR: ");
            System.out.println("http://192.168.7.13:8082/clientes/verificar?id=" + cliente.getId());
        }
    }

    // --- NUEVO: MÉTODO DE VERIFICACIÓN ---
    public boolean verificarCuenta(Long id) {
        return clienteRepository.findById(id).map(cliente -> {
            if (cliente.isVerificado()) return true;

            cliente.setVerificado(true);
            clienteRepository.save(cliente);
            return true;
        }).orElse(false);
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
        user.setTelefono(clienteDetails.getTelefono());

        if (clienteDetails.getContrasenya() != null && !clienteDetails.getContrasenya().isBlank()) {
            user.setContrasenya(passwordEncoder.encode(clienteDetails.getContrasenya()));
        }

        // 🔹 Atributos específicos (Cliente)
        if (user instanceof Cliente cliente) {
            cliente.setDireccion(clienteDetails.getDireccion());
            cliente.setObservacion(clienteDetails.getObservacion());
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
            cliente.setDireccion(clienteDetails.getDireccion() != null ? clienteDetails.getDireccion() : cliente.getDireccion());
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


}