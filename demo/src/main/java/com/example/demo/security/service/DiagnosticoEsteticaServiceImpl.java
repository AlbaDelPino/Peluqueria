package com.example.demo.security.service;

import com.example.demo.domain.Cliente;
import com.example.demo.domain.DiagnosticoEstetica;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.DiagnosticoEsteticaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiagnosticoEsteticaServiceImpl implements DiagnosticoEsteticaService {

    private final DiagnosticoEsteticaRepository diagnosticoEsteticaRepository;
    private final ClienteRepository clienteRepository;

    public DiagnosticoEsteticaServiceImpl(DiagnosticoEsteticaRepository diagnosticoEsteticaRepository, ClienteRepository clienteRepository) {
        this.diagnosticoEsteticaRepository = diagnosticoEsteticaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<DiagnosticoEstetica> getAllDiagnosticos() {
        return diagnosticoEsteticaRepository.findAll();
    }

    @Override
    public Optional<DiagnosticoEstetica> getDiagnosticoByClienteId(Long clienteId) {
        return diagnosticoEsteticaRepository.findByClienteId(clienteId);
    }

    @Override
    public Optional<DiagnosticoEstetica> getDiagnosticoById(Long id) {
        return diagnosticoEsteticaRepository.findById(id);
    }

    @Override
    @Transactional
    public DiagnosticoEstetica saveOrUpdateDiagnostico(Long clienteId, DiagnosticoEstetica diagnosticoEstetica) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        // Buscar si ya existe un diagnóstico estético para este cliente
        Optional<DiagnosticoEstetica> existente = diagnosticoEsteticaRepository.findByClienteId(clienteId);

        DiagnosticoEstetica entity;
        if (existente.isPresent()) {
            // Actualizar el existente conservando su ID
            entity = existente.get();
            entity.setTipoAlimentacion(diagnosticoEstetica.getTipoAlimentacion());
            entity.setConsumoAguaDiario(diagnosticoEstetica.getConsumoAguaDiario());
            entity.setIntoleranciasAlimentarias(diagnosticoEstetica.getIntoleranciasAlimentarias());
            entity.setTipoAgua(diagnosticoEstetica.getTipoAgua());
            entity.setFrecuenciaAlcohol(diagnosticoEstetica.getFrecuenciaAlcohol());
            entity.setSuplementacion(diagnosticoEstetica.getSuplementacion());
            entity.setHorasSueno(diagnosticoEstetica.getHorasSueno());
            entity.setCalidadSueno(diagnosticoEstetica.getCalidadSueno());
            entity.setFrecuenciaTabaco(diagnosticoEstetica.getFrecuenciaTabaco());
            entity.setHorasEjercicioSemanal(diagnosticoEstetica.getHorasEjercicioSemanal());
            entity.setTipoEjercicio(diagnosticoEstetica.getTipoEjercicio());
            entity.setProfesion(diagnosticoEstetica.getProfesion());
            entity.setHorasExposicionSolar(diagnosticoEstetica.getHorasExposicionSolar());
            entity.setExpresionesMotorasFaciales(diagnosticoEstetica.getExpresionesMotorasFaciales());
            entity.setTratamientosMedicosEsteticos(diagnosticoEstetica.getTratamientosMedicosEsteticos());
            entity.setProductosHabituales(diagnosticoEstetica.getProductosHabituales());
            entity.setNivelEstresAnsiedad(diagnosticoEstetica.getNivelEstresAnsiedad());
            entity.setObservaciones(diagnosticoEstetica.getObservaciones());
        } else {
            // Crear nuevo
            entity = diagnosticoEstetica;
            entity.setCliente(cliente);
        }

        return diagnosticoEsteticaRepository.save(entity);
    }

    @Override
    public void deleteDiagnostico(Long id) {
        diagnosticoEsteticaRepository.deleteById(id);
    }
}