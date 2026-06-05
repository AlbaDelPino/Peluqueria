package com.example.demo.security.service;

import com.example.demo.domain.Cliente;
import com.example.demo.domain.DiagnosticoPeluqueria;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.DiagnosticoPeluqueriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiagnosticoPeluqueriaServiceImpl implements DiagnosticoPeluqueriaService {

    private final DiagnosticoPeluqueriaRepository diagnosticoPeluqueriaRepository;
    private final ClienteRepository clienteRepository;

    public DiagnosticoPeluqueriaServiceImpl(DiagnosticoPeluqueriaRepository diagnosticoPeluqueriaRepository, ClienteRepository clienteRepository) {
        this.diagnosticoPeluqueriaRepository = diagnosticoPeluqueriaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<DiagnosticoPeluqueria> getAllDiagnosticos() {
        return diagnosticoPeluqueriaRepository.findAll();
    }

    @Override
    public Optional<DiagnosticoPeluqueria> getDiagnosticoByClienteId(Long clienteId) {
        return diagnosticoPeluqueriaRepository.findByClienteId(clienteId);
    }

    @Override
    public Optional<DiagnosticoPeluqueria> getDiagnosticoById(Long id) {
        return diagnosticoPeluqueriaRepository.findById(id);
    }

    @Override
    @Transactional
    public DiagnosticoPeluqueria saveOrUpdateDiagnostico(Long clienteId, DiagnosticoPeluqueria diagnosticoPeluqueria) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        cliente.setAlergenos(diagnosticoPeluqueria.getAlergenos());
        clienteRepository.save(cliente);

        // Buscar si ya existe un diagnóstico para este cliente
        Optional<DiagnosticoPeluqueria> existente = diagnosticoPeluqueriaRepository.findByClienteId(clienteId);

        DiagnosticoPeluqueria entity;
        if (existente.isPresent()) {
            // Actualizar el existente conservando su ID
            entity = existente.get();
            entity.setFrecuenciaLavado(diagnosticoPeluqueria.getFrecuenciaLavado());
            entity.setProductosUtilizados(diagnosticoPeluqueria.getProductosUtilizados());
            entity.setAlergenos(diagnosticoPeluqueria.getAlergenos());
            entity.setTipoPelo(diagnosticoPeluqueria.getTipoPelo());
            entity.setAnomalias(diagnosticoPeluqueria.getAnomalias());
            entity.setTexturaPelo(diagnosticoPeluqueria.getTexturaPelo());
            entity.setAlteraciones(diagnosticoPeluqueria.getAlteraciones());
            entity.setColorMatiz(diagnosticoPeluqueria.getColorMatiz());
            entity.setCanas(diagnosticoPeluqueria.getCanas());
            entity.setGrosor(diagnosticoPeluqueria.getGrosor());
            entity.setLongitud(diagnosticoPeluqueria.getLongitud());
            entity.setPalpacion(diagnosticoPeluqueria.getPalpacion());
            entity.setPresion(diagnosticoPeluqueria.getPresion());
            entity.setMovilizacion(diagnosticoPeluqueria.getMovilizacion());
            entity.setSignoJaquet(diagnosticoPeluqueria.getSignoJaquet());
            entity.setSignoSaboraud(diagnosticoPeluqueria.getSignoSaboraud());
            entity.setTestPapel(diagnosticoPeluqueria.getTestPapel());
            entity.setPullTest(diagnosticoPeluqueria.getPullTest());
            entity.setPuntas(diagnosticoPeluqueria.getPuntas());
            entity.setTextura(diagnosticoPeluqueria.getTextura());
            entity.setDeslizarArrastra(diagnosticoPeluqueria.getDeslizarArrastra());
            entity.setCorneometro(diagnosticoPeluqueria.getCorneometro());
            entity.setPh(diagnosticoPeluqueria.getPh());
            entity.setSebometro(diagnosticoPeluqueria.getSebometro());
            entity.setLuzWood(diagnosticoPeluqueria.getLuzWood());
            entity.setObservacionesAparato(diagnosticoPeluqueria.getObservacionesAparato());
            entity.setMicrocamara(diagnosticoPeluqueria.getMicrocamara());
            entity.setDanoEstructural(diagnosticoPeluqueria.getDanoEstructural());
            entity.setAnomaliasCongenitas(diagnosticoPeluqueria.getAnomaliasCongenitas());
            entity.setObservacionesMicro(diagnosticoPeluqueria.getObservacionesMicro());
            entity.setTricoFrontal_Anagena(diagnosticoPeluqueria.getTricoFrontal_Anagena());
            entity.setTricoFrontal_Catagena(diagnosticoPeluqueria.getTricoFrontal_Catagena());
            entity.setTricoFrontal_Telogena(diagnosticoPeluqueria.getTricoFrontal_Telogena());
            entity.setTricoTemporalIzquierdo_Anagena(diagnosticoPeluqueria.getTricoTemporalIzquierdo_Anagena());
            entity.setTricoTemporalIzquierdo_Catagena(diagnosticoPeluqueria.getTricoTemporalIzquierdo_Catagena());
            entity.setTricoTemporalIzquierdo_Telogena(diagnosticoPeluqueria.getTricoTemporalIzquierdo_Telogena());
            entity.setTricoTemporalDerecho_Anagena(diagnosticoPeluqueria.getTricoTemporalDerecho_Anagena());
            entity.setTricoTemporalDerecho_Catagena(diagnosticoPeluqueria.getTricoTemporalDerecho_Catagena());
            entity.setTricoTemporalDerecho_Telogena(diagnosticoPeluqueria.getTricoTemporalDerecho_Telogena());
            entity.setTricoParietal_Anagena(diagnosticoPeluqueria.getTricoParietal_Anagena());
            entity.setTricoParietal_Catagena(diagnosticoPeluqueria.getTricoParietal_Catagena());
            entity.setTricoParietal_Telogena(diagnosticoPeluqueria.getTricoParietal_Telogena());
            entity.setTricoOccipital_Anagena(diagnosticoPeluqueria.getTricoOccipital_Anagena());
            entity.setTricoOccipital_Catagena(diagnosticoPeluqueria.getTricoOccipital_Catagena());
            entity.setTricoOccipital_Telogena(diagnosticoPeluqueria.getTricoOccipital_Telogena());
            entity.setObservacionesTrico(diagnosticoPeluqueria.getObservacionesTrico());
        } else {
            // Crear nuevo
            entity = diagnosticoPeluqueria;
            entity.setSignoJaquet(false);
            entity.setSignoSaboraud(false);
            entity.setTestPapel(false);
            entity.setPullTest(false);
            entity.setCliente(cliente);
        }

        return diagnosticoPeluqueriaRepository.save(entity);
    }

    @Override
    public void deleteDiagnostico(Long id) {
        diagnosticoPeluqueriaRepository.deleteById(id);
    }
}
