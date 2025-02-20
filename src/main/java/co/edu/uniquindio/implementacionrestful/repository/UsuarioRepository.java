package co.edu.uniquindio.implementacionrestful.repository;

import co.edu.uniquindio.implementacionrestful.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
