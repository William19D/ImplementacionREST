package co.edu.uniquindio.implementacionrestful.controller;

import co.edu.uniquindio.implementacionrestful.dto.UserRegistrationRequest;
import co.edu.uniquindio.implementacionrestful.dto.UserResponse;
import co.edu.uniquindio.implementacionrestful.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final List<Usuario> usuarios = new ArrayList<>();
    private Long idCounter = 1L;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRegistrationRequest request) {
        Usuario usuarioCreado = new Usuario(idCounter++, request.fullName(), request.email(), request.dateBirth());
        usuarios.add(usuarioCreado);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuarioCreado.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new UserResponse(usuarioCreado.getId(), usuarioCreado.getNombre(), usuarioCreado.getEmail(), usuarioCreado.getFechaNacimiento()));
    }
}
