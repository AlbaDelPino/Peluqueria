package com.example.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "cita")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate fecha;



    @NotNull
    // 👈 guarda el nombre del enum en la BD
    private boolean estado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_horario", nullable = false)
    private HorarioSemanal horario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    public Cita() {}

    public Cita( LocalDate fecha, boolean estado, HorarioSemanal horario, Cliente cliente) {

        this.fecha = fecha;
        this.estado = estado;
        this.horario = horario;
        this.cliente = cliente;

    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public boolean isEstado() {
        return estado;
    }





    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public HorarioSemanal getHorario() { return horario; }
    public void setHorario(HorarioSemanal horario) { this.horario = horario; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}
