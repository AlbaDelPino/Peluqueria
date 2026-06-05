package com.example.demo.security.service;

import com.example.demo.domain.Cliente;
import com.example.demo.domain.ERole;
import com.example.demo.domain.User;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.DiagnosticoPeluqueriaRepository;
import com.example.demo.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
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
    private final DiagnosticoPeluqueriaRepository diagnosticoPeluqueriaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public ClienteService(UserRepository userRepository,
                          ClienteRepository clienteRepository,
                          DiagnosticoPeluqueriaRepository diagnosticoPeluqueriaRepository,
                          PasswordEncoder passwordEncoder,
                          JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.diagnosticoPeluqueriaRepository = diagnosticoPeluqueriaRepository;
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
        try {
            enviarCorreoVerificacion(nuevoCliente);
        } catch (Exception e) {
            System.err.println("Error enviando correo: " + e.getMessage());
        }

        diagnosticoPeluqueriaRepository.findByClienteId(cliente.getId()).ifPresent(diagnostico -> {
            diagnostico.setAlergenos(cliente.getAlergenos());
            diagnosticoPeluqueriaRepository.save(diagnostico);
        });

        return nuevoCliente;
    }
    private void enviarCorreoVerificacion(Cliente cliente) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // Configuramos el helper para permitir contenido HTML y codificación UTF-8
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 1. Configuración de cabeceras (Obligatorio para Gmail)
            helper.setFrom("albadelpino1@gmail.com"); // 👈 Evita que Google lo bloquee por remitente anónimo
            helper.setTo(cliente.getEmail());
            helper.setSubject("Activa tu cuenta - Bernat Experience");

            // 2. Construcción del enlace y contenido HTML con tu IP actual
            String urlVerificacion = "http://217.154.179.135:8080/peluqueriaia/clientes/verificar?id=" + cliente.getId();

            String htmlContent = "<h3>¡Hola " + cliente.getNombre() + "!</h3>" +
                    "<p>Gracias por registrarte en Bernat Experience. Pulsa el siguiente enlace para activar tu cuenta:</p>" +
                    "<br>" +
                    "<a href='" + urlVerificacion + "' style='background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-weight: bold;'>ACTIVAR CUENTA</a>" +
                    "<br><br>" +
                    "<p>Si el botón no funciona, puedes copiar y pegar este enlace en tu navegador:</p>" +
                    "<p>" + urlVerificacion + "</p>";

            // El segundo parámetro 'true' indica que el texto contiene código HTML
            helper.setText(htmlContent, true);

            // 3. Envío del correo electrónico
            mailSender.send(message);
            System.out.println("✅ CORREO ENVIADO CON ÉXITO A: " + cliente.getEmail());

        } catch (Exception e) {
            System.err.println("❌ ERROR AL ENVIAR CORREO: " + e.getMessage());

            // PLAN B: Si el servidor SMTP o Gmail fallan, imprimimos el link en consola
            System.out.println("⚠️ USA ESTE LINK MANUALMENTE PARA VERIFICAR AL USUARIO: ");
            System.out.println("http://217.154.179.135:8080/peluqueriaia/clientes/verificar?id=" + cliente.getId());
        }
    }

    // --- NUEVO: MÉTODO DE VERIFICACIÓN ---

    public boolean verificarCliente(Long id) {
        // Buscamos al usuario en la base de datos
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent() && optionalUser.get() instanceof Cliente cliente) {
            // Cambiamos el estado a true
            cliente.setVerificado(true);
            // Guardamos los cambios
            userRepository.save(cliente);
            System.out.println("✅ Cliente con ID " + id + " verificado con éxito.");
            return true;
        }

        System.err.println("❌ No se pudo verificar: No existe el cliente con ID " + id);
        return false;
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

        diagnosticoPeluqueriaRepository.findByClienteId(id).ifPresent(diagnostico -> {
            diagnostico.setAlergenos(clienteDetails.getAlergenos());
            diagnosticoPeluqueriaRepository.save(diagnostico);
        });

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

        diagnosticoPeluqueriaRepository.findByClienteId(clienteDetails.getId()).ifPresent(diagnostico -> {
            diagnostico.setAlergenos(clienteDetails.getAlergenos());
            diagnosticoPeluqueriaRepository.save(diagnostico);
        });

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