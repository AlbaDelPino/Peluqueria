package com.example.demo.security.service;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
            user.setTelefono(updatedUser.getTelefono());
            user.setContrasenya(updatedUser.getContrasenya());
            user.setEstado(updatedUser.isEstado());
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
    public List<User> getUsersByEstado(boolean estado) {
        return userRepository.findByEstado(estado);
    }


}
