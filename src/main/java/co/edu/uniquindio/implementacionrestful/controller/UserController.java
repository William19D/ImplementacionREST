package co.edu.uniquindio.implementacionrestful.controller;

import co.edu.uniquindio.implementacionrestful.dto.UserRegistrationRequest;
import co.edu.uniquindio.implementacionrestful.dto.UserResponse;
import co.edu.uniquindio.implementacionrestful.model.Usuario;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRegistrationRequest request) {
        if (request.email().isBlank() || request.fullName().isBlank()) {
            return ResponseEntity.badRequest().body("El email y el nombre no pueden estar vacíos");
        }

        Usuario usuario = new Usuario(idCounter++, request.fullName(), request.email(), request.dateBirth());
        usuarios.add(usuario);
        return ResponseEntity.ok(new UserResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getFechaNacimiento()));
    }

    // ✅ GET - Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        if (usuarios.isEmpty()) {
            return ResponseEntity.badRequest().body("No hay usuarios registrados.");
        }

        List<UserResponse> response = usuarios.stream()
                .map(u -> new UserResponse(u.getId(), u.getNombre(), u.getEmail(), u.getFechaNacimiento()))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarios.stream().filter(u -> u.getId().equals(id)).findFirst();

        if (usuario.isPresent()) {
            Usuario u = usuario.get();
            return ResponseEntity.ok(new UserResponse(u.getId(), u.getNombre(), u.getEmail(), u.getFechaNacimiento()));
        } else {
            return ResponseEntity.badRequest().body("Usuario no encontrado con ID: " + id);
        }
    }


    // ✅ PUT - Actualizar un usuario
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserRegistrationRequest request) {
        Optional<Usuario> usuarioOptional = usuarios.stream().filter(u -> u.getId().equals(id)).findFirst();

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("No se encontró un usuario con ID: " + id);
        }

        Usuario usuario = usuarioOptional.get();
        usuario.setNombre(request.fullName());
        usuario.setEmail(request.email());
        usuario.setFechaNacimiento(request.dateBirth());

        return ResponseEntity.ok(new UserResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getFechaNacimiento()));
    }

    // ✅ DELETE - Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean removed = usuarios.removeIf(u -> u.getId().equals(id));
        if (!removed) {
            return ResponseEntity.badRequest().body("No se encontró un usuario con ID: " + id);
        }
        return ResponseEntity.noContent().build();
    }
}
