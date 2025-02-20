package co.edu.uniquindio.implementacionrestful.controller;

import co.edu.uniquindio.implementacionrestful.dto.UserRegistrationRequest;
import co.edu.uniquindio.implementacionrestful.dto.UserResponse;
import co.edu.uniquindio.implementacionrestful.model.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final List<Usuario> usuarios = new ArrayList<>();
    private Long idCounter = 1L;

    // ✅ POST - Crear Usuario
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRegistrationRequest request) {
        Usuario usuario = new Usuario(idCounter++, request.fullName(), request.email(), request.dateBirth());
        usuarios.add(usuario);
        return ResponseEntity.ok(new UserResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getFechaNacimiento()));
    }

    // ✅ GET - Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> response = usuarios.stream()
                .map(u -> new UserResponse(u.getId(), u.getNombre(), u.getEmail(), u.getFechaNacimiento()))
                .toList();
        return ResponseEntity.ok(response);
    }

    // ✅ GET - Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarios.stream().filter(u -> u.getId().equals(id)).findFirst();
        return usuario.map(u -> ResponseEntity.ok(new UserResponse(u.getId(), u.getNombre(), u.getEmail(), u.getFechaNacimiento())))
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ PUT - Actualizar un usuario
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRegistrationRequest request) {
        Optional<Usuario> usuarioOptional = usuarios.stream().filter(u -> u.getId().equals(id)).findFirst();
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setNombre(request.fullName());
            usuario.setEmail(request.email());
            usuario.setFechaNacimiento(request.dateBirth());
            return ResponseEntity.ok(new UserResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getFechaNacimiento()));
        }
        return ResponseEntity.notFound().build();
    }

    // ✅ DELETE - Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean removed = usuarios.removeIf(u -> u.getId().equals(id));
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
