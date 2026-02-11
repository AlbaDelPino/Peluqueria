package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @ManyToMany
    @JoinTable(
            name = "bloqueo_horario_semanal",
            joinColumns = @JoinColumn(name = "id_bloqueo"),
            inverseJoinColumns = @JoinColumn(name = "id_horario")
    )
    private List<HorarioSemanal> horarios = new ArrayList<>();

    public BloqueoHorario() {
    }

    public BloqueoHorario( LocalDate fecha, boolean recurrente, List<HorarioSemanal> horarios) {
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
