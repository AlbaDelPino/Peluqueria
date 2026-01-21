package com.example.demo.repository;

import com.example.demo.domain.ServicioImagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServicioImagenRepository extends JpaRepository<ServicioImagen, Long> {

    @Query("SELECT si FROM ServicioImagen si WHERE si.servicio.id = :servicioId")
    List<ServicioImagen> findByServicioId(@Param("servicioId") Long servicioId);
}