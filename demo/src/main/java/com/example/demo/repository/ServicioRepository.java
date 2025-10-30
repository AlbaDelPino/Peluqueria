package com.example.demo.repository;
import com.example.demo.domain.Servicio;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
public interface ServicioRepository extends CrudRepository<Servicio, Long> {
    Set<Servicio> findAll();
    Set<Servicio> findByNombre(String nombre);
    Optional<Servicio> findById(long id);
    Set<Servicio> findByDescripcion(String descripcion);
    Set<Servicio> findByPrecio(long precio);
    Set<Servicio> findByDuracion(long duracion);
    List<Servicio> findByNombreODescripcion(String nombre, String descripcion);
    List<Servicio> findByNombreYDuracion(String nombre, long duracion);
    List<Servicio> findByNombreYPrecio(String nombre, long precio);


    List<Servicio> findByNombreAndDuracion(String nombre, long duracion);

    // Query nativa: buscar por nombre LIKE y edad >
    @Query(value = "SELECT * FROM alumno WHERE nombre LIKE %:nombre% AND edad > :precio", nativeQuery = true)
    List<Servicio> findByNombreLikeAndPrecioGreaterThan(@Param("nombre") String nombre, @Param("precio") int edad);
}
