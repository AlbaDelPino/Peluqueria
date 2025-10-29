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
    Set<Servicio> findByDescripcion(String descripcion);
    Set<Servicio> findByPrecio(long precio);
    Optional<Servicio> findById(long id);
    List<Servicio> buscarPorNombreODescripcion(String nombre, String descripcion);
    List<Servicio> buscarPorNombreYDuracion(String nombre, long duracion);
    List<Servicio> buscarPorNombreLikeYPrecio(String nombre, long precio);


    // Query nativa: buscar por nombre LIKE y edad >
    /*@Query(value = "SELECT * FROM alumno WHERE nombre LIKE %:nombre% AND edad > :edad", nativeQuery = true)
    List<Servicio> findByNombreLikeAndEdadGreaterThan(@Param("nombre") String nombre, @Param("edad") int edad);*/
}
