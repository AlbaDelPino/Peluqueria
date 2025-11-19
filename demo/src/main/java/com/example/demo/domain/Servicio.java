package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Long id_servicio;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio")
    private Long precio;

    @Column(name = "duracion")
    private Long duracion;

    // 🔑 Opción A: cambiamos LAZY → EAGER
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_servicio", nullable = false)
    @JsonBackReference
    private TipoServicio tipoServicio;

    public Servicio() {
        this.nombre = "";
        this.descripcion = "";
        this.precio = 0L;
        this.duracion = 0L;
    }

    public Servicio(String nombre, String descripcion, Long precio, Long duracion, TipoServicio tipoServicio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracion = duracion;
        this.tipoServicio = tipoServicio;
    }

    // Getters y Setters
    public Long getId_servicio() { return id_servicio; }
    public void setId_servicio(Long id_servicio) { this.id_servicio = id_servicio; }

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

    @Override
    public String toString() {
        return "Servicio{" +
                "id_servicio=" + id_servicio +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", duracion=" + duracion +
                ", tipoServicio=" + (tipoServicio != null ? tipoServicio.getNombre() : "null") +
                '}';
    }
}
