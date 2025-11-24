package com.example.demo.repository;
import com.example.demo.domain.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    List<Servicio> findAll();
    Set<Servicio> findByNombre(String nombre);
    Optional<Servicio> findById(long id);
    Set<Servicio> findByDescripcion(String descripcion);
    Set<Servicio> findByPrecio(long precio);
    Set<Servicio> findByDuracion(long duracion);
    List<Servicio> findByNombreOrDescripcion(String nombre, String descripcion);
    List<Servicio> findByNombreAndDuracion(String nombre, long duracion);
  //  List<Servicio> findByNombreAndPrecio(String nombre, long precio);



    @Query("SELECT s FROM Servicio s WHERE s.tipoServicio.id = :tipoServicioId")
    List<Servicio> findByTipoId(@Param("tipoServicioId") Long tipoServicioId);

    // Query nativa: buscar por nombre LIKE y precio >
    @Query(value = "SELECT * FROM servicio WHERE nombre LIKE CONCAT('%', :nombre, '%') AND precio > :precio", nativeQuery = true)
    List<Servicio> findByNombreAndPrecio(@Param("nombre") String nombre, @Param("precio") long precio);


}
