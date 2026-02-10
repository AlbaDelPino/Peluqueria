package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "clientes")
public class Cliente extends User {

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true) private List<ComentarioCita> comentarioCitas;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "cliente_servicios_favoritos", // Nombre de la tabla que se creará
            joinColumns = @JoinColumn(name = "id"), // Referencia al 'id' de User
            inverseJoinColumns = @JoinColumn(name = "id_servicio") // Referencia al 'id_servicio' de Servicio
    )
    private java.util.Set<Servicio> favoritos = new java.util.HashSet<>();

    @Column(nullable = false)
    private boolean verificado = false;

    @Column(nullable = false, length = 15)
    private long telefono;

    public Cliente() {}

    public Cliente(String username, String nombre, String email, long telefono, String contrasenya, boolean estado, String observacion, String alergenos, byte[] imagen) { super(username, nombre, email, contrasenya, estado, ERole.ROLE_CLIENTE); this.observacion = observacion; setTelefono(telefono);this.alergenos = alergenos; this.imagen = imagen; }

    public Cliente(String username, String nombre, String email, String contrasenya, boolean estado, ERole role, List<ComentarioCita> comentarioCitas, String observacion, String alergenos, byte[] imagen, List<Cita> citas, Set<Servicio> favoritos, boolean verificado) {
        super(username, nombre, email, contrasenya, estado, role);
        this.comentarioCitas = comentarioCitas;
        this.observacion = observacion;
        this.alergenos = alergenos;
        this.imagen = imagen;
        this.citas = citas;
        this.favoritos = favoritos;
        this.verificado = verificado;
    }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public String getAlergenos() { return alergenos; }
    public void setAlergenos(String alergenos) { this.alergenos = alergenos; }

    public byte[] getImagen() { return imagen; }
    public void setImagen(byte[] imagen) { this.imagen = imagen; }

    public List<Cita> getCitas() { return citas; }
    public void setCitas(List<Cita> citas) { this.citas = citas; }
    public java.util.Set<Servicio> getFavoritos() {
        return favoritos;
    }

    public long getTelefono() {return telefono;}
    public void setTelefono(long telefono) {
        if (telefono != 0 && telefono != 111111111L) {
            if (telefono < 100000000L || telefono > 999999999999999L) {
                throw new IllegalArgumentException("El teléfono debe tener entre 9 y 15 dígitos.");
            }
        }
        this.telefono = telefono;
    }
    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    public void setFavoritos(java.util.Set<Servicio> favoritos) {
        this.favoritos = favoritos;
    }
}