package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "servicio_imagenes")
public class ServicioImagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "datos", columnDefinition = "LONGTEXT")
    private String datos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servicio")
    @JsonBackReference
    private Servicio servicio;

    public ServicioImagen() {}

    public ServicioImagen(Long id, String datos, Servicio servicio) {
        this.id = id;
        this.datos = datos;
        this.servicio = servicio;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }
}