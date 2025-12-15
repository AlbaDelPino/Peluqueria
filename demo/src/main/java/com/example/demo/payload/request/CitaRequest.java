package com.example.demo.payload.request;

import java.time.LocalDate;
import java.time.LocalTime;

public class CitaRequest {
    private LocalDate fecha;
    private LocalTime hora;
    private Long clienteId;
    private Long servicioId;

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Long getServicioId() { return servicioId; }
    public void setServicioId(Long servicioId) { this.servicioId = servicioId; }
}
