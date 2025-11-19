package com.example.demo.security.service;

import com.example.demo.domain.Cliente;
import com.example.demo.domain.ERole;
import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final UserRepository userRepository;

    public ClienteService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        return userRepository.save(cliente);
    }

    // Actualizar un cliente
    public User updateCliente(Long id, Cliente clienteDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) return null;

        User user = optionalUser.get();
        if (!(user instanceof Cliente)) return null;

        Cliente cliente = (Cliente) user;

        // 🔹 Atributos comunes
        cliente.setNombre(clienteDetails.getNombre() != null ? clienteDetails.getNombre() : cliente.getNombre());
        cliente.setUsername(clienteDetails.getUsername() != null ? clienteDetails.getUsername() : cliente.getUsername());
        cliente.setEmail(clienteDetails.getEmail() != null ? clienteDetails.getEmail() : cliente.getEmail());
        if (clienteDetails.getContrasenya() != null && !clienteDetails.getContrasenya().isEmpty()) {
            cliente.setContrasenya(clienteDetails.getContrasenya());
        }

        // 🔹 Atributos específicos
        cliente.setDireccion(clienteDetails.getDireccion() != null ? clienteDetails.getDireccion() : cliente.getDireccion());
        cliente.setObservacion(clienteDetails.getObservacion() != null ? clienteDetails.getObservacion() : cliente.getObservacion());
        cliente.setAlergenos(clienteDetails.getAlergenos() != null ? clienteDetails.getAlergenos() : cliente.getAlergenos());

        return userRepository.save(cliente);
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
