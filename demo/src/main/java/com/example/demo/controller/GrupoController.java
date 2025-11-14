package com.example.demo.controller;

import com.example.demo.domain.Grupo;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    @Autowired
    private UserRepository userRepository;

    // Obtener todos los grupos
    @GetMapping("/")
    public List<Grupo> getAllGrupos() {
        return userRepository.findAll().stream()
                .filter(u -> u instanceof Grupo)
                .map(u -> (Grupo) u)
                .toList();
    }

    // Obtener grupo por username
    @GetMapping("/{username}")
    public ResponseEntity<Grupo> getGrupo(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .filter(u -> u instanceof Grupo)
                .map(u -> ResponseEntity.ok((Grupo) u)) // devolvemos la entidad Grupo
                .orElse(ResponseEntity.notFound().build()); // devuelve 404 si no existe
    }

    // Puedes agregar más métodos específicos para Grupo aquí
}
