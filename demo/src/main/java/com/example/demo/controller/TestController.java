package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestController {

  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/cliente")
  @PreAuthorize("hasRole('ROLE_CLIENTE')")
  public String clienteAccess() {
    return "Cliente Content.";
  }

  @GetMapping("/grupo")
  @PreAuthorize("hasRole('ROLE_GRUPO') or hasRole('ROLE_CLIENTE')")
  public String grupoAccess() {
    return "Grupo Content.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_GRUPO') or hasRole('ROLE_CLIENTE')")
  public String adminAccess() {
    return "Admin Content.";
  }
}
