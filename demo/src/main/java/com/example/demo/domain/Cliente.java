package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

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

    @Lob

    private byte[] imagen;
    // Usamos byte[] para mapear un BLOB
    @OneToMany(mappedBy = "cliente", orphanRemoval = true)
    @JsonIgnore
    private List<Cita> citas;

    public Cliente() {}

    public Cliente(String username, String nombre, String email, long telefono, String contrasenya, boolean estado,
                   String direccion, String observacion, String alergenos, byte[] imagen) {
        super(username, nombre, email, telefono, contrasenya, estado, ERole.ROLE_CLIENTE);
        this.direccion = direccion;
        this.observacion = observacion;
        this.alergenos = alergenos;
        this.imagen = imagen;
    }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public String getAlergenos() { return alergenos; }
    public void setAlergenos(String alergenos) { this.alergenos = alergenos; }

    public byte[] getImagen() { return imagen; }
    public void setImagen(byte[] imagen) { this.imagen = imagen; }

    public List<Cita> getCitas() { return citas; }
    public void setCitas(List<Cita> citas) { this.citas = citas; }
}