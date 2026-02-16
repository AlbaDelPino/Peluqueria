package com.example.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "valoracion")
public class Valoracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_valoracion")
    private Long idValoracion;

    @Column(name = "comentario", nullable = false)
    private String comentario;

    @Column
    @Min(0)
    @Max(5)
    private int puntuacion;

    @Column
    @Min(0)
    @Max(5)
    private int trato;

    @Column
    @Min(0)
    @Max(5)
    private int desarrollo;

    @Column
    @Min(0)
    @Max(5)
    private int comunicacion;

    @Column
    @Min(0)
    @Max(5)
    private int organizacion;

    @Lob
    private byte[] imagen;

    @OneToOne
    @JoinColumn(name = "id_cita", nullable = false, unique = true)
    @com.fasterxml.jackson.annotation.JsonIgnore // <--- Añade esto
    private Cita cita;

    public Valoracion() {
    }

    public Valoracion(String comentario, int puntuacion, int trato, int desarrollo, int comunicacion, int organizacion, byte[] imagen, Cita cita) {
        this.comentario = comentario;
        this.puntuacion = puntuacion;
        this.trato = trato;
        this.desarrollo = desarrollo;
        this.comunicacion = comunicacion;
        this.organizacion = organizacion;
        this.imagen = imagen;
        this.cita = cita;
    }

    // Getters y setters
    public Long getIdValoracion() { return idValoracion; }
    public void setIdValoracion(Long idValoracion) { this.idValoracion = idValoracion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public int getPuntuacion() { return puntuacion; }
    public void setPuntuacion(@Min(0) @Max(5) int puntuacion) { this.puntuacion = puntuacion; }

    public int getTrato() {
        return trato;
    }
    public void setTrato(@Min(0) @Max(5) int trato) {
        this.trato = trato;
    }

    public int getDesarrollo() {
        return desarrollo;
    }
    public void setDesarrollo(@Min(0) @Max(5) int desarrollo) {
        this.desarrollo = desarrollo;
    }

    public int getComunicacion() {
        return comunicacion;
    }
    public void setComunicacion(@Min(0) @Max(5) int comunicacion) {
        this.comunicacion = comunicacion;
    }

    public int getOrganizacion() {
        return organizacion;
    }
    public void setOrganizacion(@Min(0) @Max(5) int organizacion) {
        this.organizacion = organizacion;
    }

    public byte[] getImagen() { return imagen; }
    public void setImagen(byte[] imagen) { this.imagen = imagen; }

    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }
}
