package com.example.demo.security.service;



import com.example.demo.domain.Cliente;
import com.example.demo.domain.Servicio;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.ServicioRepository;
import com.example.demo.security.service.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class FavoritoServiceImpl implements FavoritoService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Override
    @Transactional
    public void agregarAFavoritos(Long clienteId, Long servicioId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));

        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + servicioId));

        cliente.getFavoritos().add(servicio);
        clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public void eliminarDeFavoritos(Long clienteId, Long servicioId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        cliente.getFavoritos().remove(servicio);
        clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Servicio> listarFavoritos(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        return cliente.getFavoritos();
    }
}