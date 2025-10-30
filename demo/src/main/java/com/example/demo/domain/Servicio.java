package com.example.demo.domain;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id_servicio"
)
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private long id_servicio;

    @Column(name = "precio")
    private long precio;
    @Column(name = "duracion")
    private long duracion;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "nombre")
    private String nombre;

    public Servicio(long id_servicio, long precio, long duracion, String descripcion, String nombre) {
        this.id_servicio = id_servicio;
        this.precio = precio;
        this.duracion = duracion;
        this.descripcion = descripcion;
        this.nombre = nombre;
    }
    public Servicio() {

        this.precio = 0;
        this.duracion = 0;
        this.descripcion = "";
        this.nombre = "";
    }

    public long getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(long id_servicio) {
        this.id_servicio = id_servicio;
    }

    public long getPrecio() {
        return precio;
    }

    public void setPrecio(long precio) {
        this.precio = precio;
    }

    public long getDuracion() {
        return duracion;
    }

    public void setDuracion(long duracion) {
        this.duracion = duracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Servicio{" +
                "id_servicio=" + id_servicio +
                ", precio=" + precio +
                ", duracion=" + duracion +
                ", descripcion='" + descripcion + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
