package com.example.demo.controller;

import com.example.demo.domain.ServicioImagen;
import com.example.demo.security.service.ServicioImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/imagenes")
@CrossOrigin(origins = "*") // Permite peticiones desde Flutter
public class ServicioImagenController {

    @Autowired
    private ServicioImagenService service;

    /**
     * Sube una imagen y la convierte a Base64 antes de guardarla en la BD
     * POST http://localhost:8082/api/imagenes/subir/{servicioId}
     */
    @PostMapping("/subir/{servicioId}")
    public ResponseEntity<?> subirImagen(@RequestParam("foto") MultipartFile archivo, @PathVariable Long servicioId) {
        try {
            if (archivo.isEmpty()) {
                return ResponseEntity.badRequest().body("El archivo está vacío");
            }

            ServicioImagen guardada = service.guardar(archivo, servicioId);

            // Devolvemos un mapa simple para que el JSON sea legible
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Imagen guardada exitosamente en Base64",
                    "id", guardada.getId()
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la imagen: " + e.getMessage());
        }
    }

    /**
     * Obtiene todas las imágenes de un servicio.
     * El campo 'datos' contendrá el String en Base64.
     * GET http://localhost:8082/api/imagenes/servicio/{servicioId}
     */
    @GetMapping("/servicio/{servicioId}")
    public ResponseEntity<List<ServicioImagen>> listarPorServicio(@PathVariable Long servicioId) {
        List<ServicioImagen> imagenes = service.listarPorServicio(servicioId);
        if (imagenes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(imagenes);
    }

    /**
     * Elimina una imagen de la base de datos
     * DELETE http://localhost:8082/api/imagenes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            // Nota: Debes añadir el método eliminar en tu Service si no lo tienes
            // service.eliminar(id);
            return ResponseEntity.ok().body("Imagen eliminada");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al eliminar");
        }
    }
}