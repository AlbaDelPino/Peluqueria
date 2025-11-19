package com.example.demo.security.service;

import com.example.demo.domain.TipoServicio;
import java.util.List;
import java.util.Optional;

public interface TipoServicioService {
    List<TipoServicio> findAll();
    Optional<TipoServicio> findById(Long id);
    TipoServicio addTipoServicio(TipoServicio tipoServicio);
    void deleteTipoServicio(Long id);
}
