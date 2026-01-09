package com.example.demo.security.service;


import com.example.demo.domain.Servicio;
import java.util.Set;

public interface FavoritoService {
    void agregarAFavoritos(Long clienteId, Long servicioId);
    void eliminarDeFavoritos(Long clienteId, Long servicioId);
    Set<Servicio> listarFavoritos(Long clienteId);
}