package com.example.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "grupos")
public class Grupo extends User {

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String curso;

    @NotBlank
    @Size(max = 30)
    @Column(nullable = false, length = 30)
    private String turno;

    public Grupo() {}

    public Grupo(String username, String nombre, String email, Long telefono, String contrasenya, boolean estado,
                 String curso, String turno) {
        super(username, nombre, email, telefono, contrasenya, estado, ERole.ROLE_GRUPO);
        this.curso = curso;
        this.turno = turno;
    }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
}
