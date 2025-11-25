package com.example.demo.payload.request;

import com.example.demo.domain.ERole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupRequest {

    // --- Campos comunes ---
    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 70)
    private String nombre;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 100)
    private String contrasenya;

    // --- Campos específicos de Admin ---
    private String especialidad;

    // --- Campos específicos de Grupo ---
    private String curso;
    private String turno;

    // --- Getters y Setters ---
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasenya() { return contrasenya; }
    public void setContrasenya(String contrasenya) { this.contrasenya = contrasenya; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
}
