package com.example.demo.security.service;

import com.example.demo.domain.ServicioImagen;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface ServicioImagenService {
    ServicioImagen guardar(MultipartFile archivo, Long servicioId) throws IOException;
    List<ServicioImagen> listarPorServicio(Long servicioId);
    ServicioImagen obtenerPorId(Long id);
}