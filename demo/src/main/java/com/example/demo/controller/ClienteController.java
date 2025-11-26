package com.example.demo.controller;

import com.example.demo.domain.Cliente;
import com.example.demo.domain.User;
import com.example.demo.security.service.ClienteService;
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


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GRUPO','CLIENTE)")

    public Cliente updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        return (Cliente) clienteService.updateCliente(id, cliente);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GRUPO','CLIENTE)")

    public boolean deleteCliente(@PathVariable Long id) {
        return clienteService.deleteCliente(id);
    }
}
