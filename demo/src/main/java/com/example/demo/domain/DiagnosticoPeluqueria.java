package com.example.demo.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "diagnostico_peluqueria")
public class DiagnosticoPeluqueria {

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
    private boolean signoJaquet;
    private boolean signoSaboraud;
    private boolean testPapel;

    // Exploración Manual de Tallo Capilar
    private boolean pullTest;
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
    private String tricoFrontal_Anagena;
    private String tricoFrontal_Catagena;
    private String tricoFrontal_Telogena;

    private String tricoTemporalIzquierdo_Anagena;
    private String tricoTemporalIzquierdo_Catagena;
    private String tricoTemporalIzquierdo_Telogena;

    private String tricoTemporalDerecho_Anagena;
    private String tricoTemporalDerecho_Catagena;
    private String tricoTemporalDerecho_Telogena;

    private String tricoParietal_Anagena;
    private String tricoParietal_Catagena;
    private String tricoParietal_Telogena;

    private String tricoOccipital_Anagena;
    private String tricoOccipital_Catagena;
    private String tricoOccipital_Telogena;

    @Column(length = 2000)
    private String observacionesTrico;

    // Constructores
    public DiagnosticoPeluqueria() {}

    public DiagnosticoPeluqueria(Cliente cliente) {
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

    public boolean getSignoJaquet() {
        return signoJaquet;
    }

    public void setSignoJaquet(boolean signoJaquet) {
        this.signoJaquet = signoJaquet;
    }

    public boolean getSignoSaboraud() {
        return signoSaboraud;
    }

    public void setSignoSaboraud(boolean signoSaboraud) {
        this.signoSaboraud = signoSaboraud;
    }

    public boolean getTestPapel() {
        return testPapel;
    }

    public void setTestPapel(boolean testPapel) {
        this.testPapel = testPapel;
    }

    public boolean getPullTest() {
        return pullTest;
    }

    public void setPullTest(boolean pullTest) {
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

    public String getTricoFrontal_Anagena() {
        return tricoFrontal_Anagena;
    }

    public void setTricoFrontal_Anagena(String tricoFrontal_Anagena) {
        this.tricoFrontal_Anagena = tricoFrontal_Anagena;
    }

    public String getTricoFrontal_Catagena() {
        return tricoFrontal_Catagena;
    }

    public void setTricoFrontal_Catagena(String tricoFrontal_Catagena) {
        this.tricoFrontal_Catagena = tricoFrontal_Catagena;
    }

    public String getTricoFrontal_Telogena() {
        return tricoFrontal_Telogena;
    }

    public void setTricoFrontal_Telogena(String tricoFrontal_Telogena) {
        this.tricoFrontal_Telogena = tricoFrontal_Telogena;
    }

    public String getTricoTemporalIzquierdo_Anagena() {
        return tricoTemporalIzquierdo_Anagena;
    }

    public void setTricoTemporalIzquierdo_Anagena(String tricoTemporalIzquierdo_Anagena) {
        this.tricoTemporalIzquierdo_Anagena = tricoTemporalIzquierdo_Anagena;
    }

    public String getTricoTemporalIzquierdo_Catagena() {
        return tricoTemporalIzquierdo_Catagena;
    }

    public void setTricoTemporalIzquierdo_Catagena(String tricoTemporalIzquierdo_Catagena) {
        this.tricoTemporalIzquierdo_Catagena = tricoTemporalIzquierdo_Catagena;
    }

    public String getTricoTemporalIzquierdo_Telogena() {
        return tricoTemporalIzquierdo_Telogena;
    }

    public void setTricoTemporalIzquierdo_Telogena(String tricoTemporalIzquierdo_Telogena) {
        this.tricoTemporalIzquierdo_Telogena = tricoTemporalIzquierdo_Telogena;
    }

    public String getTricoTemporalDerecho_Anagena() {
        return tricoTemporalDerecho_Anagena;
    }

    public void setTricoTemporalDerecho_Anagena(String tricoTemporalDerecho_Anagena) {
        this.tricoTemporalDerecho_Anagena = tricoTemporalDerecho_Anagena;
    }

    public String getTricoTemporalDerecho_Catagena() {
        return tricoTemporalDerecho_Catagena;
    }

    public void setTricoTemporalDerecho_Catagena(String tricoTemporalDerecho_Catagena) {
        this.tricoTemporalDerecho_Catagena = tricoTemporalDerecho_Catagena;
    }

    public String getTricoTemporalDerecho_Telogena() {
        return tricoTemporalDerecho_Telogena;
    }

    public void setTricoTemporalDerecho_Telogena(String tricoTemporalDerecho_Telogena) {
        this.tricoTemporalDerecho_Telogena = tricoTemporalDerecho_Telogena;
    }

    public String getTricoParietal_Anagena() {
        return tricoParietal_Anagena;
    }

    public void setTricoParietal_Anagena(String tricoParietal_Anagena) {
        this.tricoParietal_Anagena = tricoParietal_Anagena;
    }

    public String getTricoParietal_Catagena() {
        return tricoParietal_Catagena;
    }

    public void setTricoParietal_Catagena(String tricoParietal_Catagena) {
        this.tricoParietal_Catagena = tricoParietal_Catagena;
    }

    public String getTricoParietal_Telogena() {
        return tricoParietal_Telogena;
    }

    public void setTricoParietal_Telogena(String tricoParietal_Telogena) {
        this.tricoParietal_Telogena = tricoParietal_Telogena;
    }

    public String getTricoOccipital_Anagena() {
        return tricoOccipital_Anagena;
    }

    public void setTricoOccipital_Anagena(String tricoOccipital_Anagena) {
        this.tricoOccipital_Anagena = tricoOccipital_Anagena;
    }

    public String getTricoOccipital_Catagena() {
        return tricoOccipital_Catagena;
    }

    public void setTricoOccipital_Catagena(String tricoOccipital_Catagena) {
        this.tricoOccipital_Catagena = tricoOccipital_Catagena;
    }

    public String getTricoOccipital_Telogena() {
        return tricoOccipital_Telogena;
    }

    public void setTricoOccipital_Telogena(String tricoOccipital_Telogena) {
        this.tricoOccipital_Telogena = tricoOccipital_Telogena;
    }

    public String getObservacionesTrico() {
        return observacionesTrico;
    }

    public void setObservacionesTrico(String observacionesTrico) {
        this.observacionesTrico = observacionesTrico;
    }
}
