package com.example.demo.controller;

import com.example.demo.domain.Grupo;
import com.example.demo.domain.User;
import com.example.demo.security.service.GrupoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    private final GrupoService grupoService;

    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<User> getAllGrupos() {
        return grupoService.getAllGrupos();
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Grupo updateGrupo(@PathVariable Long id, @RequestBody Grupo grupo) {
        return (Grupo) grupoService.updateGrupo(id, grupo);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public boolean deleteGrupo(@PathVariable Long id) {
        return grupoService.deleteGrupo(id);
    }
}
