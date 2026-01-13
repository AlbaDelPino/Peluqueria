package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Long idServicio;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio")
    private Long precio;

    @Column(name = "duracion")
    private Long duracion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_servicio", nullable = false)
    private TipoServicio tipoServicio;

    @ManyToMany(mappedBy = "favoritos")
    @JsonIgnore // Importante para evitar bucles infinitos en el JSON
    private Set<Cliente> clientesQueMeFavorecen = new HashSet<>();

    public Servicio() {

    }

    public Servicio(String nombre, String descripcion, Long precio, Long duracion, TipoServicio tipoServicio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracion = duracion;
        this.tipoServicio = tipoServicio;
    }

    // Getters y Setters
    public Long getId_servicio() { return idServicio; }
    public void setId_servicio(Long id_servicio) { this.idServicio = id_servicio; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Long getPrecio() { return precio; }
    public void setPrecio(Long precio) { this.precio = precio; }

    public Long getDuracion() { return duracion; }
    public void setDuracion(Long duracion) { this.duracion = duracion; }

    public TipoServicio getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(TipoServicio tipoServicio) { this.tipoServicio = tipoServicio; }

    public Set<Cliente> getClientesQueMeFavorecen() {
        return clientesQueMeFavorecen;
    }

    public void setClientesQueMeFavorecen(Set<Cliente> clientesQueMeFavorecen) {
        this.clientesQueMeFavorecen = clientesQueMeFavorecen;
    }

    @Override
    public String toString() {
        return "Servicio{" +
                "id_servicio=" + idServicio +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", duracion=" + duracion +
                ", tipoServicio=" + (tipoServicio != null ? tipoServicio.getNombre() : "null") +
                '}';
    }
}
