package com.example.demo.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "diagnostico")
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    private Cliente cliente;

    // Hábitos e historial
    private String frecuenciaLavado;
    private String productosUtilizados;
    private String alergenos;

    // Estado del Cabello / Peluquería
    private String tipoPelo;
    private String anomalias;
    private String texturaPelo;
    private String alteraciones;
    private String colorMatiz;
    private String canas;
    private String grosor;
    private String longitud;

    // Exploración Manual de Cuero Cabelludo
    private String palpacion;
    private String presion;
    private String movilizacion;
    private String signoJaquet;
    private String signoSaboraud;
    private String testPapel;

    // Exploración Manual de Tallo Capilar
    private String pullTest;
    private String puntas;
    private String textura;
    private String deslizarArrastra;

    // Exploración Aparatología
    private String corneometro;
    private String ph;
    private String sebometro;
    private String luzWood;

    @Column(length = 2000)
    private String observacionesAparato;

    // Exploración Microvisor
    private String microcamara;
    private String danoEstructural;
    private String anomaliasCongenitas;

    @Column(length = 2000)
    private String observacionesMicro;

    // Tricograma
    private String tricoFrontal;
    private String tricoTemporalIzquierdo;
    private String tricoTemporalDerecho;
    private String tricoParietal;
    private String tricoOccipital;

    @Column(length = 2000)
    private String observacionesTrico;

    // Constructores
    public Diagnostico() {}

    public Diagnostico(Cliente cliente) {
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

    public String getFrecuenciaLavado() {
        return frecuenciaLavado;
    }

    public void setFrecuenciaLavado(String frecuenciaLavado) {
        this.frecuenciaLavado = frecuenciaLavado;
    }

    public String getProductosUtilizados() {
        return productosUtilizados;
    }

    public void setProductosUtilizados(String productosUtilizados) {
        this.productosUtilizados = productosUtilizados;
    }

    public String getAlergenos() {
        return alergenos;
    }

    public void setAlergenos(String alergenos) {
        this.alergenos = alergenos;
    }

    public String getTipoPelo() {
        return tipoPelo;
    }

    public void setTipoPelo(String tipoPelo) {
        this.tipoPelo = tipoPelo;
    }

    public String getAnomalias() {
        return anomalias;
    }

    public void setAnomalias(String anomalias) {
        this.anomalias = anomalias;
    }

    public String getTexturaPelo() {
        return texturaPelo;
    }

    public void setTexturaPelo(String texturaPelo) {
        this.texturaPelo = texturaPelo;
    }

    public String getAlteraciones() {
        return alteraciones;
    }

    public void setAlteraciones(String alteraciones) {
        this.alteraciones = alteraciones;
    }

    public String getColorMatiz() {
        return colorMatiz;
    }

    public void setColorMatiz(String colorMatiz) {
        this.colorMatiz = colorMatiz;
    }

    public String getCanas() {
        return canas;
    }

    public void setCanas(String canas) {
        this.canas = canas;
    }

    public String getGrosor() {
        return grosor;
    }

    public void setGrosor(String grosor) {
        this.grosor = grosor;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getPalpacion() {
        return palpacion;
    }

    public void setPalpacion(String palpacion) {
        this.palpacion = palpacion;
    }

    public String getPresion() {
        return presion;
    }

    public void setPresion(String presion) {
        this.presion = presion;
    }

    public String getMovilizacion() {
        return movilizacion;
    }

    public void setMovilizacion(String movilizacion) {
        this.movilizacion = movilizacion;
    }

    public String getSignoJaquet() {
        return signoJaquet;
    }

    public void setSignoJaquet(String signoJaquet) {
        this.signoJaquet = signoJaquet;
    }

    public String getSignoSaboraud() {
        return signoSaboraud;
    }

    public void setSignoSaboraud(String signoSaboraud) {
        this.signoSaboraud = signoSaboraud;
    }

    public String getTestPapel() {
        return testPapel;
    }

    public void setTestPapel(String testPapel) {
        this.testPapel = testPapel;
    }

    public String getPullTest() {
        return pullTest;
    }

    public void setPullTest(String pullTest) {
        this.pullTest = pullTest;
    }

    public String getPuntas() {
        return puntas;
    }

    public void setPuntas(String puntas) {
        this.puntas = puntas;
    }

    public String getTextura() {
        return textura;
    }

    public void setTextura(String textura) {
        this.textura = textura;
    }

    public String getDeslizarArrastra() {
        return deslizarArrastra;
    }

    public void setDeslizarArrastra(String deslizarArrastra) {
        this.deslizarArrastra = deslizarArrastra;
    }

    public String getCorneometro() {
        return corneometro;
    }

    public void setCorneometro(String corneometro) {
        this.corneometro = corneometro;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getSebometro() {
        return sebometro;
    }

    public void setSebometro(String sebometro) {
        this.sebometro = sebometro;
    }

    public String getLuzWood() {
        return luzWood;
    }

    public void setLuzWood(String luzWood) {
        this.luzWood = luzWood;
    }

    public String getObservacionesAparato() {
        return observacionesAparato;
    }

    public void setObservacionesAparato(String observacionesAparato) {
        this.observacionesAparato = observacionesAparato;
    }

    public String getMicrocamara() {
        return microcamara;
    }

    public void setMicrocamara(String microcamara) {
        this.microcamara = microcamara;
    }

    public String getDanoEstructural() {
        return danoEstructural;
    }

    public void setDanoEstructural(String danoEstructural) {
        this.danoEstructural = danoEstructural;
    }

    public String getAnomaliasCongenitas() {
        return anomaliasCongenitas;
    }

    public void setAnomaliasCongenitas(String anomaliasCongenitas) {
        this.anomaliasCongenitas = anomaliasCongenitas;
    }

    public String getObservacionesMicro() {
        return observacionesMicro;
    }

    public void setObservacionesMicro(String observacionesMicro) {
        this.observacionesMicro = observacionesMicro;
    }

    public String getTricoFrontal() {
        return tricoFrontal;
    }

    public void setTricoFrontal(String tricoFrontal) {
        this.tricoFrontal = tricoFrontal;
    }

    public String getTricoTemporalIzquierdo() {
        return tricoTemporalIzquierdo;
    }

    public void setTricoTemporalIzquierdo(String tricoTemporalIzquierdo) {
        this.tricoTemporalIzquierdo = tricoTemporalIzquierdo;
    }

    public String getTricoTemporalDerecho() {
        return tricoTemporalDerecho;
    }

    public void setTricoTemporalDerecho(String tricoTemporalDerecho) {
        this.tricoTemporalDerecho = tricoTemporalDerecho;
    }

    public String getTricoParietal() {
        return tricoParietal;
    }

    public void setTricoParietal(String tricoParietal) {
        this.tricoParietal = tricoParietal;
    }

    public String getTricoOccipital() {
        return tricoOccipital;
    }

    public void setTricoOccipital(String tricoOccipital) {
        this.tricoOccipital = tricoOccipital;
    }

    public String getObservacionesTrico() {
        return observacionesTrico;
    }

    public void setObservacionesTrico(String observacionesTrico) {
        this.observacionesTrico = observacionesTrico;
    }
}
