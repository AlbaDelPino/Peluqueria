package com.example.demo.security.service;

import com.example.demo.domain.ERole;
import com.example.demo.domain.Grupo;
import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrupoService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public GrupoService(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Obtener todos los grupos
    public List<User> getAllGrupos() {
        return userRepository.findByRole(ERole.ROLE_GRUPO);
    }

    // Obtener grupo por ID
    public User getGrupoById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.filter(u -> u.getRole() == ERole.ROLE_GRUPO).orElse(null);
    }

    // Crear un grupo
    public Grupo createGrupo(Grupo grupo) {
        grupo.setRole(ERole.ROLE_GRUPO);
        return userRepository.save(grupo);
    }

    // Actualizar un grupo
    public User updateGrupo(Long id, Grupo grupoDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) return null;

        User user = optionalUser.get();
        if (!(user instanceof Grupo)) return null;

        Grupo grupo = (Grupo) user;

        // 🔹 Atributos comunes (User)
        grupo.setNombre(grupoDetails.getNombre() != null ? grupoDetails.getNombre() : grupo.getNombre());
        grupo.setUsername(grupoDetails.getUsername() != null ? grupoDetails.getUsername() : grupo.getUsername());
        grupo.setEmail(grupoDetails.getEmail() != null ? grupoDetails.getEmail() : grupo.getEmail());
        grupo.setTelefono(grupoDetails.getTelefono() != 0 ? grupoDetails.getTelefono() : grupo.getTelefono());
        grupo.setEstado(grupoDetails.isEstado());
        if (grupoDetails.getContrasenya() != null && !grupoDetails.getContrasenya().isEmpty()) {
            grupo.setContrasenya(passwordEncoder.encode(grupoDetails.getContrasenya()));
        }

        // 🔹 Atributos específicos de Grupo
        grupo.setCurso(grupoDetails.getCurso() != null ? grupoDetails.getCurso() : grupo.getCurso());
        grupo.setTurno(grupoDetails.getTurno() != null ? grupoDetails.getTurno() : grupo.getTurno());

        return userRepository.save(grupo);
    }


    // Eliminar un grupo
    public boolean deleteGrupo(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get();
        if (user.getRole() != ERole.ROLE_GRUPO) return false;

        userRepository.deleteById(id);
        return true;
    }
}
