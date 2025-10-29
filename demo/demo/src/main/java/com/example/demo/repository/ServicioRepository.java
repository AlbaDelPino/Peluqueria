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
    Set<Servicio> findByDescripcion(String descripcion);
    Set<Servicio> findByprecio(long precio);

    List<Servicio> findByNombreOrDescripcion(String nombre, String descripcion);

    // Buscar por nombre Y edad
    List<Servicio> findByNombreAndDuracion(String nombre, long duracion);

    // Query nativa: buscar por nombre LIKE y edad >
    @Query(value = "SELECT * FROM alumno WHERE nombre LIKE %:nombre% AND edad > :precio", nativeQuery = true)
    List<Servicio> findByNombreLikeAndPrecioGreaterThan(@Param("nombre") String nombre, @Param("precio") int edad);
}
