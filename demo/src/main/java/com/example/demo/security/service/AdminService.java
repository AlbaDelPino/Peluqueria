package com.example.demo.security.service;

import com.example.demo.domain.Admin;
import com.example.demo.domain.ERole;
import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Obtener todos los administradores
    public List<User> getAllAdmins() {
        return userRepository.findByRole(ERole.ROLE_ADMIN);
    }

    // Obtener un administrador por ID
    public User getAdminById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.filter(u -> u.getRole() == ERole.ROLE_ADMIN).orElse(null);
    }

    // Crear un administrador
    public Admin createAdmin(Admin admin) {
        admin.setRole(ERole.ROLE_ADMIN);
        return userRepository.save(admin);
    }


    public User updateAdmin(Long id, Admin adminDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) return null;

        User user = optionalUser.get();
        if (!(user instanceof Admin)) return null;

        Admin admin = (Admin) user;

        // 🔹 Atributos comunes (User)
        admin.setNombre(adminDetails.getNombre() != null ? adminDetails.getNombre() : admin.getNombre());
        admin.setUsername(adminDetails.getUsername() != null ? adminDetails.getUsername() : admin.getUsername());
        admin.setEmail(adminDetails.getEmail() != null ? adminDetails.getEmail() : admin.getEmail());
        if (adminDetails.getContrasenya() != null && !adminDetails.getContrasenya().isEmpty()) {
            admin.setContrasenya(adminDetails.getContrasenya());
        }

        // 🔹 Atributo específico de Admin
        admin.setEspecialidad(adminDetails.getEspecialidad() != null ? adminDetails.getEspecialidad() : admin.getEspecialidad());

        return userRepository.save(admin);
    }


    // Eliminar un administrador
    public boolean deleteAdmin(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get();
        if (user.getRole() != ERole.ROLE_ADMIN) return false;

        userRepository.deleteById(id);
        return true;
    }
}
