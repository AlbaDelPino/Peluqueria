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

    @Column(name = "puntuacion", nullable = false)
    @Min(0)
    @Max(5)
    private long puntuacion;

    @Lob
    private byte[] imagen;

    // ⭐ Relación 1:1 con Cita
    @OneToOne
    @JoinColumn(name = "id_cita", nullable = false, unique = true)
    private Cita cita;

    // Getters y setters
    public Long getIdValoracion() { return idValoracion; }
    public void setIdValoracion(Long idValoracion) { this.idValoracion = idValoracion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public long getPuntuacion() { return puntuacion; }
    public void setPuntuacion(long puntuacion) { this.puntuacion = puntuacion; }

    public byte[] getImagen() { return imagen; }
    public void setImagen(byte[] imagen) { this.imagen = imagen; }

    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }
}
