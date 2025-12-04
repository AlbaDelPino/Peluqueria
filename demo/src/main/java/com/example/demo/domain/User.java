package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String username;

    @NotBlank
    @Size(max = 70)
    @Column(nullable = false, length = 70)
    private String nombre;

    @Size(max = 50)
    @Email
    @Column(length = 50)
    private String email;

    @Max(999999999)
    @Min(111111111)
    private Long telefono;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String contrasenya;

    @NotNull
    private boolean estado;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ERole role;

    public User() {}

    public User(String username, String nombre, String email, Long telefono, String contrasenya,boolean estado, ERole role) {
        this.username = username;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.contrasenya = contrasenya;
        this.estado = estado;
        this.role = role;
    }

    // Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(@NotBlank @Size(max = 20) String username) { this.username = username; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getTelefono() {return telefono;}
    public void setTelefono(Long telefono) {this.telefono = telefono;}

    public String getContrasenya() { return contrasenya; }
    public void setContrasenya(String contrasenya) { this.contrasenya = contrasenya; }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public ERole getRole() { return role; }
    public void setRole(ERole role) { this.role = role; }

    // UserDetails
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return contrasenya;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
