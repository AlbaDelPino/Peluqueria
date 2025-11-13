package com.example.demo.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente extends User {

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "alergenos")
    private String alergenos;

    public Cliente(String username, String email, String password, String direccion, String observaciones, String alergenos) {
        super(username, email, password, ERole.ROLE_CLIENTE);
        this.direccion = direccion;
        this.observaciones = observaciones;
        this.alergenos = alergenos;
    }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getAlergenos() { return alergenos; }
    public void setAlergenos(String alergenos) { this.alergenos = alergenos; }
}
