package co.edu.uniquindio.implementacionrestful.controller;

import co.edu.uniquindio.implementacionrestful.dto.UserRegistrationRequest;
import co.edu.uniquindio.implementacionrestful.dto.UserResponse;
import co.edu.uniquindio.implementacionrestful.model.Usuario;
import co.edu.uniquindio.implementacionrestful.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UsuarioRepository usuarioRepository;

    public UserController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // ✅ POST - Crear Usuario
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRegistrationRequest request) {
        if (request.email().isBlank() || request.fullName().isBlank()) {
            return ResponseEntity.badRequest().body("El email y el nombre no pueden estar vacíos");
        }

        Usuario usuario = new Usuario(null, request.fullName(), request.email(), request.dateBirth());
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new UserResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getFechaNacimiento()));
    }

    // ✅ GET - Obtener todos los usuarios con paginación
    @GetMapping
    public ResponseEntity<Page<Usuario>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        return ResponseEntity.ok(usuarios);
    }

    // ✅ GET - Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);

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
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("No se encontró un usuario con ID: " + id);
        }

        Usuario usuario = usuarioOptional.get();
        usuario.setNombre(request.fullName());
        usuario.setEmail(request.email());
        usuario.setFechaNacimiento(request.dateBirth());

        usuarioRepository.save(usuario);
        return ResponseEntity.ok(new UserResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getFechaNacimiento()));
    }

    // ✅ DELETE - Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("No se encontró un usuario con ID: " + id);
        }

        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
