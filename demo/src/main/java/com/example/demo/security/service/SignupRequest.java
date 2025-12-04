package com.example.demo.security.service;

import jakarta.validation.constraints.*;

public class SignupRequest {

    // --- Campos comunes ---
    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 70)
    private String nombre;

    @Size(max = 50)
    @Email
    private String email;

    private long telefono;

    @NotBlank
    @Size(max = 100)
    private String contrasenya;

    private boolean estado;

    // --- Campos específicos de Admin ---
    private String especialidad;

    // --- Campos específicos de Grupo ---
    private String curso;
    private String turno;

    //--role ---
    private String rol;

    // --- Campos específicos de Cliente ---
    private String direccion;
    private String observacion;
    private String alergenos;

    private byte[] imagen;   // ahora byte[] para ser coherente con Cliente


    // --- Getters y Setters ---
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public long getTelefono() { return telefono; }
    public void setTelefono(long telefono) { this.telefono = telefono; }

    public String getContrasenya() { return contrasenya; }
    public void setContrasenya(String contrasenya) { this.contrasenya = contrasenya; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public String getAlergenos() { return alergenos; }
    public void setAlergenos(String alergenos) { this.alergenos = alergenos; }

    public byte[] getImagen() { return imagen; }
    public void setImagen(byte[] imagen) { this.imagen = imagen; }
}