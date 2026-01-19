package com.example.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalTime;
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
    private Long id;

    @NotNull
    @Column(name = "dia_semana")
    private String diaSemana;

    @NotNull
    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @NotNull
    @Column(name = "hora_fin")
    private LocalTime horaFin;

    @NotNull
    private long plazas;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_servicio", nullable = false)
    private Servicio servicio;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_grupo", nullable = false)
    private Grupo grupo;

    @OneToMany(mappedBy = "horario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cita> citas;

    public HorarioSemanal() {
    }

    public HorarioSemanal(Long id, String diaSemana, LocalTime horaInicio, LocalTime horaFin, long plazas, Servicio servicio, Grupo grupo) {
        this.id = id;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.plazas = plazas;
        this.servicio = servicio;
        this.grupo = grupo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
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
                "id=" + id +
                ", diaSemana='" + diaSemana + '\'' +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", plazas=" + plazas +
                ", idServicio=" + servicio +
                ", idGrupo=" + grupo +
                '}';
    }
}
