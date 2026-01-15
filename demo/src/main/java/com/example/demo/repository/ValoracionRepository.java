package com.example.demo.repository;


import com.example.demo.domain.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ValoracionRepository extends JpaRepository<Valoracion, Long> {

    // Evita valorar dos veces la misma cita
    boolean existsByCita_Id(Long idCita);


    List<Valoracion> findByCita_Cliente_Id(Long clienteId);

}
