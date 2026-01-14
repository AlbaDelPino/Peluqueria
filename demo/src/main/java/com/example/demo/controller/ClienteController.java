package com.example.demo.controller;

import com.example.demo.domain.Cliente;
import com.example.demo.domain.User;
import com.example.demo.security.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GRUPO')")
    public List<User> getAllClientes() {
        return clienteService.getAllClientes();
    }
    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyRole('ADMIN','GRUPO','CLIENTE')")
    public User getClienteByUsername(@PathVariable String username) {
        return clienteService.getClienteByUsername(username);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GRUPO','CLIENTE')")
    public User updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        return clienteService.updateCliente(id, cliente);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GRUPO','CLIENTE')")
    public boolean deleteCliente(@PathVariable Long id) {
        return clienteService.deleteCliente(id);
    }

    /**
     * 1. ENDPOINT DE VERIFICACIÓN
     * Este es el método que se ejecuta cuando el cliente hace clic en el enlace del correo.
     */
    @GetMapping("/verificar")
    public ResponseEntity<String> verificarCuenta(@RequestParam Long id) {
        boolean verificado = clienteService.verificarCuenta(id);

        if (verificado) {
            // Retornamos un HTML para que el navegador del usuario se vea profesional
            return ResponseEntity.ok(
                    "<html><body style='font-family: Arial, sans-serif; text-align: center; padding-top: 50px;'>" +
                            "<div style='max-width: 400px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 10px;'>" +
                            "<h1 style='color: #FF6B00;'>¡Cuenta Activada!</h1>" +
                            "<p style='color: #333;'>Tu cuenta en <strong>Bernat Experience</strong> ha sido verificada correctamente.</p>" +
                            "<p>Ya puedes volver a la aplicación e iniciar sesión.</p>" +
                            "</div></body></html>"
            );
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("<h1>Error de verificación</h1><p>El enlace ha expirado o el usuario no existe.</p>");
        }
    }

    /**
     * 2. REGISTRO DE CLIENTE
     * Recibe los datos desde la pantalla de Registro de Flutter.
     */
    @PostMapping("/registro")
    public ResponseEntity<Cliente> registrar(@RequestBody Cliente cliente) {
        try {
            Cliente nuevo = clienteService.createCliente(cliente);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

