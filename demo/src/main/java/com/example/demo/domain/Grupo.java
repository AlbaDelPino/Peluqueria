package com.example.demo.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "grupos")
public class Grupo extends User {

    @Column(name = "curso")
    private String curso;
    @Column(name = "turno")
    private String turno;

    public Grupo(String username, String email, String password, ERole role, String curso, String turno) {
        super(username, email, password, ERole.ROLE_GRUPO);
        this.curso = curso;
        this.turno = turno;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
}

