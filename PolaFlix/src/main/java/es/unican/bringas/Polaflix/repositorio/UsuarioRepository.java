package es.unican.dae.repositorios;

import es.unican.dae.dominio.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /** Busca un usuario por su nombre (insensible a mayúsculas). Usado en login y búsqueda. */
    Optional<Usuario> findByNombreUsuarioIgnoreCase(String nombreUsuario);

    /** Autenticación: busca usuario por nombre y contraseña. */
    Optional<Usuario> findByNombreUsuarioIgnoreCaseAndContraseña(
            String nombreUsuario, String contraseña);
}
