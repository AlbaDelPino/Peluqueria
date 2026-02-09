package com.example.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "bloqueo_horario")
public class BloqueoHorario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private boolean recurrente;

    @OneToMany(mappedBy = "bloqueo", cascade = CascadeType.DETACH)
    private List<HorarioSemanal> horarios;

    public BloqueoHorario() {
    }

    public BloqueoHorario(Long id, LocalDate fecha, boolean recurrente, List<HorarioSemanal> horarios) {
        this.id = id;
        this.fecha = fecha;
        this.recurrente = recurrente;
        this.horarios = horarios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public boolean isRecurrente() {
        return recurrente;
    }

    public void setRecurrente(boolean recurrente) {
        this.recurrente = recurrente;
    }

    public List<HorarioSemanal> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioSemanal> horarios) {
        this.horarios = horarios;
    }

    public void addHorario(HorarioSemanal horario ){
        this.horarios.add(horario);
    }

}
