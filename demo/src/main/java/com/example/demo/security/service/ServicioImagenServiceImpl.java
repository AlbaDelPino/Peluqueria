package com.example.demo.security.service;

import com.example.demo.domain.Servicio;
import com.example.demo.domain.ServicioImagen;
import com.example.demo.repository.ServicioImagenRepository;
import com.example.demo.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class ServicioImagenServiceImpl implements ServicioImagenService {

    @Autowired
    private ServicioImagenRepository repository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Override
    public ServicioImagen guardar(MultipartFile archivo, Long servicioId) throws IOException {
        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        // Convertir bytes a String Base64
        byte[] bytes = archivo.getBytes();
        String base64Image = Base64.getEncoder().encodeToString(bytes);

        ServicioImagen img = new ServicioImagen();
        img.setDatos(base64Image); // Guardamos el texto
        img.setServicio(servicio);

        return repository.save(img);
    }

    @Override
    public List<ServicioImagen> listarTodas() {
        return repository.findAll();
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<ServicioImagen> listarPorServicio(Long servicioId) {
        return repository.findByServicioId(servicioId);
    }

    @Override
    public ServicioImagen obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }
}