package scheduler;

import com.example.demo.domain.Cita;
import com.example.demo.domain.Cliente;
import com.example.demo.domain.FcmToken;
import com.example.demo.repository.CitaRepository;
import com.example.demo.security.service.FCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ReminderTask {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private FCMService fcmService;

    // Se ejecuta cada día a las 9 AM
    @Scheduled(cron = "0 * * * * *")
    @Transactional(readOnly = true)
    public void enviarRecordatorios() {
        System.out.println("⏰ Revisando citas para mañana...");

        // Buscamos las citas que son mañana usando tu Query Nativa
        List<Cita> citasDeManana = citaRepository.buscarCitasParaManana();

        for (Cita cita : citasDeManana) {
            Cliente cliente = cita.getCliente();

            // Obtenemos la lista de todos los dispositivos de este cliente
            List<FcmToken> dispositivos = cliente.getTokens();

            if (dispositivos.isEmpty()) {
                System.out.println("⚠️ El cliente " + cliente.getNombre() + " no tiene dispositivos registrados.");
                continue;
            }

            // Enviamos la notificación a cada móvil/tablet que tenga el cliente
            for (FcmToken dispositivo : dispositivos) {
                fcmService.enviarNotificacion(
                        dispositivo.getToken(),
                        "Recordatorio Bernat Experience",
                        "Hola " + cliente.getNombre() + ", mañana tienes cita a las " + cita.getHoraInicio()
                );
            }
        }
    }
}