package com.example.demo.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "diagnostico_estetica")
public class DiagnosticoEstetica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    private Cliente cliente;

    // Hábitos y estilo de vida
    private String tipoAlimentacion;
    private String consumoAguaDiario;
    private String intoleranciasAlimentarias;
    private String tipoAgua;
    private String frecuenciaAlcohol;
    private String suplementacion;
    private String horasSueno;
    private String calidadSueno;
    private String frecuenciaTabaco;
    private String horasEjercicioSemanal;
    private String tipoEjercicio;
    private String profesion;
    private String horasExposicionSolar;
    private String expresionesMotorasFaciales;
    private String tratamientosMedicosEsteticos;
    private String productosHabituales;
    private String nivelEstresAnsiedad;

    @Column(length = 2000)
    private String observaciones;

    // Constructores
    public DiagnosticoEstetica() {}

    public DiagnosticoEstetica(Cliente cliente) {
        this.cliente = cliente;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getTipoAlimentacion() {
        return tipoAlimentacion;
    }

    public void setTipoAlimentacion(String tipoAlimentacion) {
        this.tipoAlimentacion = tipoAlimentacion;
    }

    public String getConsumoAguaDiario() {
        return consumoAguaDiario;
    }

    public void setConsumoAguaDiario(String consumoAguaDiario) {
        this.consumoAguaDiario = consumoAguaDiario;
    }

    public String getIntoleranciasAlimentarias() {
        return intoleranciasAlimentarias;
    }

    public void setIntoleranciasAlimentarias(String intoleranciasAlimentarias) {
        this.intoleranciasAlimentarias = intoleranciasAlimentarias;
    }

    public String getTipoAgua() {
        return tipoAgua;
    }

    public void setTipoAgua(String tipoAgua) {
        this.tipoAgua = tipoAgua;
    }

    public String getFrecuenciaAlcohol() {
        return frecuenciaAlcohol;
    }

    public void setFrecuenciaAlcohol(String frecuenciaAlcohol) {
        this.frecuenciaAlcohol = frecuenciaAlcohol;
    }

    public String getSuplementacion() {
        return suplementacion;
    }

    public void setSuplementacion(String suplementacion) {
        this.suplementacion = suplementacion;
    }

    public String getHorasSueno() {
        return horasSueno;
    }

    public void setHorasSueno(String horasSueno) {
        this.horasSueno = horasSueno;
    }

    public String getCalidadSueno() {
        return calidadSueno;
    }

    public void setCalidadSueno(String calidadSueno) {
        this.calidadSueno = calidadSueno;
    }

    public String getFrecuenciaTabaco() {
        return frecuenciaTabaco;
    }

    public void setFrecuenciaTabaco(String frecuenciaTabaco) {
        this.frecuenciaTabaco = frecuenciaTabaco;
    }

    public String getHorasEjercicioSemanal() {
        return horasEjercicioSemanal;
    }

    public void setHorasEjercicioSemanal(String horasEjercicioSemanal) {
        this.horasEjercicioSemanal = horasEjercicioSemanal;
    }

    public String getTipoEjercicio() {
        return tipoEjercicio;
    }

    public void setTipoEjercicio(String tipoEjercicio) {
        this.tipoEjercicio = tipoEjercicio;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getHorasExposicionSolar() {
        return horasExposicionSolar;
    }

    public void setHorasExposicionSolar(String horasExposicionSolar) {
        this.horasExposicionSolar = horasExposicionSolar;
    }

    public String getExpresionesMotorasFaciales() {
        return expresionesMotorasFaciales;
    }

    public void setExpresionesMotorasFaciales(String expresionesMotorasFaciales) {
        this.expresionesMotorasFaciales = expresionesMotorasFaciales;
    }

    public String getTratamientosMedicosEsteticos() {
        return tratamientosMedicosEsteticos;
    }

    public void setTratamientosMedicosEsteticos(String tratamientosMedicosEsteticos) {
        this.tratamientosMedicosEsteticos = tratamientosMedicosEsteticos;
    }

    public String getProductosHabituales() {
        return productosHabituales;
    }

    public void setProductosHabituales(String productosHabituales) {
        this.productosHabituales = productosHabituales;
    }

    public String getNivelEstresAnsiedad() {
        return nivelEstresAnsiedad;
    }

    public void setNivelEstresAnsiedad(String nivelEstresAnsiedad) {
        this.nivelEstresAnsiedad = nivelEstresAnsiedad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}