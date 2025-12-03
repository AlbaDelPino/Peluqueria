package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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


    // Relación con citas (un cliente puede tener muchas citas)
    @OneToMany(mappedBy = "cliente", orphanRemoval = true)
    @JsonIgnore
    private java.util.List<Cita> citas;

    public Cliente() {}

    public Cliente(String username, String nombre, String email, Long telefono, String contrasenya, boolean estado,
                   String direccion, String observacion, String alergenos) {
        super(username, nombre, email, telefono, contrasenya, estado, ERole.ROLE_CLIENTE);
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

    public java.util.List<Cita> getCitas() { return citas; }
    public void setCitas(java.util.List<Cita> citas) { this.citas = citas; }
}
