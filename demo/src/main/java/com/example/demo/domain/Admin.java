package com.example.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "admins")
public class Admin extends User {

    @NotBlank
    @Size(max = 50)
    @Column(name = "especialidad", nullable = false, length = 50)
    private String especialidad;

    public Admin() {}

    public Admin(String username, String nombre, String email, String contrasenya, String especialidad) {
        super(username, nombre, email, contrasenya, ERole.ROLE_ADMIN);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}
