package co.edu.uniquindio.implementacionrestful.dto;

import java.time.LocalDate;

public record UserResponse(
        Long id,
        String nombre,
        String email,
        LocalDate fechaNacimiento
) {}
