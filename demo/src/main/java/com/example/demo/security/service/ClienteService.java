package com.example.demo.security.service;

import com.example.demo.domain.Cliente;
import com.example.demo.domain.ERole;
import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        cliente.setRole(ERole.ROLE_CLIENTE);
        return userRepository.save(cliente); // 👈 ahora sí se guarda como Cliente
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
}