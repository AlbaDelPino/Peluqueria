package com.example.demo.security.service;



import com.example.demo.domain.Valoracion;

import java.util.List;

public interface ValoracionService {

    Valoracion crearValoracion(Valoracion valoracion);

    Valoracion findById(Long id);

    void deleteValoracion(Long id);
    List<Valoracion> findAllValoraciones();

    List<Valoracion> findValoracionesByCliente(Long clienteId);

    Valoracion actualizarValoracion(Long id, Valoracion valoracion);

}
