package com.example.demo.repository;
import com.example.demo.domain.Servicio;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
public interface ServicioRepository extends CrudRepository<Servicio, Long> {
    Set<Servicio> findAll();
    Set<Servicio> findByNombre(String nombre);
    Set<Servicio> findByApellido(String apellido);
    Set<Servicio> findByEdad(int edad);
    // Buscar por nombre O apellido
    List<Servicio> findByNombreOrApellido(String nombre, String apellido);

    // Buscar por nombre Y edad
    List<Servicio> findByNombreAndEdad(String nombre, int edad);

    // Query nativa: buscar por nombre LIKE y edad >
    @Query(value = "SELECT * FROM alumno WHERE nombre LIKE %:nombre% AND edad > :edad", nativeQuery = true)
    List<Servicio> findByNombreLikeAndEdadGreaterThan(@Param("nombre") String nombre, @Param("edad") int edad);
}
