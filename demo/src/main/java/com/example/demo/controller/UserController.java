package com.example.demo.controller;

import com.example.demo.domain.*;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.service.ClienteService;
import com.example.demo.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ClienteService clienteService;
    private final JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, JwtUtils jwtUtils,ClienteService clienteService,UserRepository userRepository) {
        this.userService = userService;
        this.clienteService = clienteService;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    // --- SIGNIN ---
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // 1. Buscamos al usuario (Esto ya lo tienes)
        User user = userService.getAllUsers().stream()
                .filter(u -> u.getUsername().equals(loginRequest.getUsername()))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Error: Usuario no encontrado"));
        }

        // ---------------------------------------------------------
        // 2. AÑADE ESTO AQUÍ (Control de verificación)
        // ---------------------------------------------------------
        if (user instanceof Cliente) {
            Cliente cliente = (Cliente) user;
            if (!cliente.isVerificado()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Error: Debes activar tu cuenta mediante el correo enviado."));
            }
        }
        // ---------------------------------------------------------

        // 3. Validar contraseña (Esto ya lo tienes)
        if (!passwordEncoder.matches(loginRequest.getContrasenya(), user.getContrasenya())) {
            return ResponseEntity.status(401).body(new MessageResponse("Error: Contraseña incorrecta"));
        }

        // 4. Generar Token (Esto ya lo tienes)
        String token = jwtUtils.generateJwtToken(user.getUsername(), List.of(user.getRole().name()));

        return ResponseEntity.ok(new JwtResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                List.of(user.getRole().name())
        ));
    }

    // --- SIGNUP ADMIN (solo otro admin puede crear) ---
    @PostMapping("/signup/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerAdmin(@RequestBody SignupRequest signUpRequest) {
        String encodedPassword = passwordEncoder.encode(signUpRequest.getContrasenya());

        Admin admin = new Admin(
                signUpRequest.getUsername(),
                signUpRequest.getNombre(),
                signUpRequest.getEmail(),
                signUpRequest.getTelefono(),
                encodedPassword,
                signUpRequest.isEstado(),
                signUpRequest.getEspecialidad()
        );
        admin.setRole(ERole.ROLE_ADMIN);

        userService.saveUser(admin);
        return ResponseEntity.ok(new MessageResponse("Admin registrado correctamente!"));
    }

    // --- SIGNUP GRUPO (solo admin puede crear) ---
    @PostMapping("/signup/grupo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerGrupo(@RequestBody SignupRequest signUpRequest) {
        String encodedPassword = passwordEncoder.encode(signUpRequest.getContrasenya());

        Grupo grupo = new Grupo(
                signUpRequest.getUsername(),
                signUpRequest.getNombre(),
                signUpRequest.getEmail(),
                signUpRequest.getTelefono(),
                encodedPassword,
                signUpRequest.isEstado(),
                signUpRequest.getCurso(),
                signUpRequest.getTurno()
        );
        grupo.setRole(ERole.ROLE_GRUPO);

        userService.saveUser(grupo);
        return ResponseEntity.ok(new MessageResponse("Grupo registrado correctamente!"));
    }

    @PostMapping("/signup/cliente")
    @PreAuthorize("hasAnyRole('ADMIN','GRUPO')")
    public ResponseEntity<?> registerCliente(@RequestBody SignupRequest signUpRequest) {
        String encodedPassword = passwordEncoder.encode(signUpRequest.getContrasenya());

        Cliente cliente = new Cliente(
                signUpRequest.getUsername(),
                signUpRequest.getNombre(),
                signUpRequest.getEmail(),
                signUpRequest.getTelefono(),
                encodedPassword,
                signUpRequest.isEstado(),
                signUpRequest.getDireccion(),
                signUpRequest.getObservacion(),
                signUpRequest.getAlergenos(),
                signUpRequest.getImagen()
        );
        cliente.setRole(ERole.ROLE_CLIENTE);

        userService.saveUser(cliente);
        return ResponseEntity.ok(new MessageResponse("Cliente registrado correctamente!"));
    }

    @PostMapping("/signup/cliente/public")
    public ResponseEntity<?> registerClientePublic(@RequestBody SignupRequest signUpRequest) {
        try {
            // 1. Creamos la instancia del cliente
            Cliente cliente = new Cliente(
                    signUpRequest.getUsername(),
                    signUpRequest.getNombre(),
                    signUpRequest.getEmail(),
                    signUpRequest.getTelefono(),
                    signUpRequest.getContrasenya(), // Se pasará al service para cifrarla
                    true, // estado activo
                    signUpRequest.getDireccion(),
                    signUpRequest.getObservacion(),
                    signUpRequest.getAlergenos(),
                    signUpRequest.getImagen()
            );

            // 2. Usamos el Service que ya configuramos para enviar el mail
            clienteService.createCliente(cliente);

            return ResponseEntity.ok(new MessageResponse("Registro realizado. Revisa tu email."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error al registrar: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','GRUPO','CLIENTE')")
    public ResponseEntity<?> getCurrentUser(java.security.Principal principal) {
        String username = principal.getName();

        User user = userService.getUserByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Usuario no encontrado"));
        }

        return ResponseEntity.ok(user);
    }



    // --- LISTAR todos los usuarios ---

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN','GRUPO')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // --- OBTENER usuario por ID ---
    @GetMapping("/users/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GRUPO')")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Usuario no encontrado"));
        }
        return ResponseEntity.ok(user);
    }
    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String,
                Object> data) {
        String email = (String) data.get("email");
        String nombre = (String) data.get("nombre");
        String username = (String) data.get("username");
        String googleId = (String) data.get("googleId");

        // 1. Buscamos si el usuario ya existe por email
        User user = userService.getAllUsers().stream()
                .filter(u -> u != null && u.getEmail() != null &&
                        u.getEmail().equals(email))
                .findFirst()
                .orElse(null);

        // 2. Si no existe, lo registramos como nuevo Cliente
        if (user == null) {
            Cliente nuevoCliente = new Cliente(
                    username,
                    nombre,
                    email,
                    123456789L,  // Long para el teléfono
                    passwordEncoder.encode("GOOGLE_USER_" + googleId),
                    true,        // estado activo
                    "Sin dirección",
                    "Registro vía Google",
                    "Ninguna",
                    null         // null para el BLOB de imagen
            );
            nuevoCliente.setRole(ERole.ROLE_CLIENTE);
            nuevoCliente.setVerificado(true);
            userService.saveUser(nuevoCliente);
            user = nuevoCliente;
        }

        // 3. Generamos el Token JWT
        String token = jwtUtils.generateJwtToken(user.getUsername(),
                List.of(user.getRole().name()));

        return ResponseEntity.ok(new JwtResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                List.of(user.getRole().name())
        ));
    }
    // 1. Endpoint para solicitar el correo de recuperación
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) {
        // 1. Buscamos directamente en el repositorio (más rápido y seguro)
        // Suponiendo que tienes userRepository inyectado, o usa un método en userService
        return userRepository.findByEmail(email)
                .map(user -> {
                    try {
                        // 2. Usamos el servicio para generar código y enviar
                        clienteService.enviarCorreoRecuperacion(user);
                        return ResponseEntity.ok(new MessageResponse("Correo de recuperación enviado con éxito."));
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new MessageResponse("Error al enviar el correo: " + e.getMessage()));
                    }
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse("Error: No existe un usuario con ese email.")));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String codigo = request.get("codigo");
        String nuevaPassword = request.get("nuevaPassword");

        // Buscamos directamente por email para evitar el NullPointerException de los Streams
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Usuario no encontrado."));
        }

        boolean codigoValido = clienteService.validarCodigoRecuperacion(email, codigo);

        if (!codigoValido) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Código incorrecto o expirado."));
        }

        user.setContrasenya(passwordEncoder.encode(nuevaPassword));
        userRepository.save(user); // O userService.saveUser(user);

        return ResponseEntity.ok(new MessageResponse("Contraseña actualizada correctamente."));
    }

}
