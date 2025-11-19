package com.example.demo.security.service;

import com.example.demo.domain.TipoServicio;
import com.example.demo.repository.TipoServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoServicioServiceImpl implements TipoServicioService {
    @Autowired
    private TipoServicioRepository tipoServicioRepository;

    @Override
    public List<TipoServicio> findAll() { return tipoServicioRepository.findAll(); }

    @Override
    public Optional<TipoServicio> findById(Long id) { return tipoServicioRepository.findById(id); }

    @Override
    public TipoServicio addTipoServicio(TipoServicio tipoServicio) {
        return tipoServicioRepository.save(tipoServicio);
    }

    @Override
    public void deleteTipoServicio(Long id) {
        tipoServicioRepository.deleteById(id);
    }
}
