package com.example.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "horario_semanal",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"horaInicio", "horaFin","diaSemana","id_servicio","id_grupo"})
        }
)
public class HorarioSemanal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Long id_horario;

    @NotNull
    private String diaSemana;

    @NotNull
    private LocalTime horaInicio;

    @NotNull
    private LocalTime horaFin;

    @NotNull
    private long plazas;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name="horario_servicio",
            joinColumns = {@JoinColumn(name="id_horario")},
            inverseJoinColumns = {@JoinColumn(name="id_servicio")},
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"id_horario", "id_servicio"})
            })
    private List<Servicio> servicios;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_grupo", nullable = false)
    private Grupo grupo;

    public HorarioSemanal() {
    }

    public HorarioSemanal(Long id_horario, String diaSemana, LocalTime horaInicio, LocalTime horaFin, long plazas, List<Servicio> servicios, Grupo grupo) {
        this.id_horario = id_horario;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.plazas = plazas;
        this.servicios = servicios;
        this.grupo = grupo;
    }

    public Long getId() {
        return id_horario;
    }

    public void setId(Long id) {
        this.id_horario = id_horario;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public long getPlazas() {
        return plazas;
    }

    public void setPlazas(long plazas) {
        this.plazas = plazas;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    @Override
    public String toString() {
        return "HorarioSemanal{" +
                "id=" + id_horario +
                ", diaSemana='" + diaSemana + '\'' +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", plazas=" + plazas +
                ", idGrupo=" + grupo +
                ", Servicios=" + servicios +
                '}';
    }
}
