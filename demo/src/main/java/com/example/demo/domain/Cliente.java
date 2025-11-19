package com.example.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "clientes")
public class Cliente extends User {

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String direccion;

    @Size(max = 200)
    @Column(length = 200)
    private String observacion;

    @Size(max = 200)
    @Column(length = 200)
    private String alergenos;

    public Cliente() {}

    public Cliente(String username, String nombre, String email, String contrasenya,
                   String direccion, String observacion, String alergenos) {
        super(username, nombre, email, contrasenya, ERole.ROLE_CLIENTE);
        this.direccion = direccion;
        this.observacion = observacion;
        this.alergenos = alergenos;
    }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public String getAlergenos() { return alergenos; }
    public void setAlergenos(String alergenos) { this.alergenos = alergenos; }
}
