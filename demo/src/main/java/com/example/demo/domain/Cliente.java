package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "clientes")
public class Cliente extends User {

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComentarioCita> comentarioCitas;

    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Diagnostico diagnostico;

    @Size(max = 200)
    @Column(length = 200)
    private String observacion;

    @Size(max = 200)
    @Column(length = 200)
    private String alergenos;

    @Lob
    private byte[] imagen; // Mapea un BLOB para la foto de perfil

    @OneToMany(mappedBy = "cliente", orphanRemoval = true)
    @JsonIgnore
    private List<Cita> citas;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "cliente_servicios_favoritos",
            joinColumns = @JoinColumn(name = "id"), // Referencia al 'id' de User/Cliente
            inverseJoinColumns = @JoinColumn(name = "id_servicio")
    )
    private java.util.Set<Servicio> favoritos = new java.util.HashSet<>();

    @Column(nullable = false, length = 15)
    private long telefono;

    // 🔹 NUEVO CAMPO: Controla si el usuario pulsó el enlace del correo
    @Column(nullable = false)
    private boolean verificado = false;

    // Constructor vacío (Obligatorio para JPA)
    public Cliente() {}

    // Constructor básico
    public Cliente(String username, String nombre, String email, long telefono, String contrasenya, String observacion, String alergenos, byte[] imagen) {
        super(username, nombre, email, contrasenya, ERole.ROLE_CLIENTE);
        this.observacion = observacion;
        setTelefono(telefono);
        this.alergenos = alergenos;
        this.imagen = imagen;
    }

    // Constructor completo (Corregido para asignar el booleano 'verificado')
    public Cliente(String username, String nombre, String email, String contrasenya, ERole role, List<ComentarioCita> comentarioCitas, String observacion, String alergenos, byte[] imagen, List<Cita> citas, Set<Servicio> favoritos, boolean verificado) {
        super(username, nombre, email, contrasenya, role);
        this.comentarioCitas = comentarioCitas;
        this.observacion = observacion;
        this.alergenos = alergenos;
        this.imagen = imagen;
        this.citas = citas;
        this.favoritos = favoritos;
        this.verificado = verificado; // 👈 Asignación corregida
    }

    // --- GETTERS Y SETTERS ---

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public String getAlergenos() { return alergenos; }
    public void setAlergenos(String alergenos) { this.alergenos = alergenos; }

    public byte[] getImagen() { return imagen; }
    public void setImagen(byte[] imagen) { this.imagen = imagen; }

    public List<Cita> getCitas() { return citas; }
    public void setCitas(List<Cita> citas) { this.citas = citas; }

    public java.util.Set<Servicio> getFavoritos() { return favoritos; }
    public void setFavoritos(java.util.Set<Servicio> favoritos) { this.favoritos = favoritos; }

    public Diagnostico getDiagnostico() { return diagnostico; }
    public void setDiagnostico(Diagnostico diagnostico) { this.diagnostico = diagnostico; }

    public long getTelefono() { return telefono; }
    public void setTelefono(long telefono) {
        if (telefono != 0 && telefono != 111111111L) {
            if (telefono < 100000000L || telefono > 999999999999999L) {
                throw new IllegalArgumentException("El teléfono debe tener entre 9 y 15 dígitos.");
            }
        }
        this.telefono = telefono;
    }

    // 🔹 NUEVOS METHODS: Para gestionar el estado de verificación
    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }
}