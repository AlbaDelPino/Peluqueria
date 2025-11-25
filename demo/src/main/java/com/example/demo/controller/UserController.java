package com.example.demo.controller;

import com.example.demo.domain.Admin;
import com.example.demo.domain.ERole;
import com.example.demo.domain.Grupo;
import com.example.demo.domain.User;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        User user = userService.getAllUsers().stream()
                .filter(u -> u.getUsername().equals(loginRequest.getUsername()) &&
                        u.getContrasenya().equals(loginRequest.getContrasenya()))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Error: Usuario o contraseña incorrecta"));
        }

        String token = jwtUtils.generateJwtToken(user.getUsername());

        return ResponseEntity.ok(new JwtResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                List.of(user.getRole().name())
        ));
    }

    // REGISTRO
    // --- SIGNUP ADMIN ---
    @PostMapping("/signup/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody SignupRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username ya está en uso!"));
        }
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email ya está en uso!"));
        }

        Admin admin = new Admin(
                signUpRequest.getUsername(),
                signUpRequest.getNombre(),
                signUpRequest.getEmail(),
                signUpRequest.getContrasenya(),
                signUpRequest.getEspecialidad()
        );

        userService.saveUser(admin);
        return ResponseEntity.ok(new MessageResponse("Admin registrado correctamente!"));
    }

    // --- SIGNUP GRUPO ---
    @PostMapping("/signup/grupo")
    public ResponseEntity<?> registerGrupo(@RequestBody SignupRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username ya está en uso!"));
        }
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email ya está en uso!"));
        }

        Grupo grupo = new Grupo(
                signUpRequest.getUsername(),
                signUpRequest.getNombre(),
                signUpRequest.getEmail(),
                signUpRequest.getContrasenya(),
                signUpRequest.getCurso(),
                signUpRequest.getTurno()
        );

        userService.saveUser(grupo);
        return ResponseEntity.ok(new MessageResponse("Grupo registrado correctamente!"));
    }

    // --- SIGNUP CLIENTE ---
    @PostMapping("/signup/cliente")
    public ResponseEntity<?> registerCliente(@RequestBody SignupRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username ya está en uso!"));
        }
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email ya está en uso!"));
        }

        User cliente = new User(
                signUpRequest.getUsername(),
                signUpRequest.getNombre(),
                signUpRequest.getEmail(),
                signUpRequest.getContrasenya(),
                ERole.ROLE_CLIENTE
        );

        userService.saveUser(cliente);
        return ResponseEntity.ok(new MessageResponse("Cliente registrado correctamente!"));
    }

        // LISTAR todos los usuarios
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // OBTENER usuario por ID
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Usuario no encontrado"));
        }
        return ResponseEntity.ok(user);
    }

    // ACTUALIZAR usuario
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser)
                .map(user -> ResponseEntity.ok(new MessageResponse("Usuario actualizado correctamente")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse("Usuario no encontrado")));
    }


    // ELIMINAR usuario
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok(new MessageResponse("Usuario eliminado correctamente"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Usuario no encontrado"));
        }
    }
}
