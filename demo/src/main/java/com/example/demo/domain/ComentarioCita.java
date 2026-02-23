package com.example.demo.domain;


import jakarta.persistence.*;

@Entity
@Table(name = "comentario_cita")
public class ComentarioCita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000, nullable = false)
    private String comentario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id",referencedColumnName = "id", nullable = false)
    private Cliente cliente;

    public ComentarioCita() {}

    public ComentarioCita(String comentario, Cliente cliente) {
        this.comentario = comentario;
        this.cliente = cliente;
    }

    public Long getId() { return id; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}
