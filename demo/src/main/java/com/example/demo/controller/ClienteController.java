package com.example.demo.controller;

import com.example.demo.domain.Cliente;
import com.example.demo.domain.User;
import com.example.demo.security.service.ClienteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<User> getAllClientes() {
        return clienteService.getAllClientes();
    }

    @PostMapping
    public Cliente createCliente(@RequestBody Cliente cliente) {
        return clienteService.createCliente(cliente);
    }

    @PutMapping("/{id}")
    public Cliente updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        return (Cliente) clienteService.updateCliente(id, cliente);
    }


    @DeleteMapping("/{id}")
    public boolean deleteCliente(@PathVariable Long id) {
        return clienteService.deleteCliente(id);
    }
}
