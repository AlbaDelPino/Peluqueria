/*package com.example.demo.security.service;

import com.example.demo.domain.Cita;
import com.example.demo.domain.EstadoCita;
import com.example.demo.repository.CitaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CitaCleanupService {

    private final CitaRepository citaRepository;

    public CitaCleanupService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    // Ejecutar cada día a las 03:00 AM
    @Scheduled(cron = "0 0 3 * * ?")
    public void eliminarCitasCompletadasAntiguas() {

        LocalDate limite = LocalDate.now().minusDays(30);

        List<Cita> citas = citaRepository.findByEstadoAndFechaBefore(
                EstadoCita.COMPLETADO,
                limite
        );

        citas.forEach(citaRepository::delete);
    }
}*/
