package com.paola.todo.repository;

import com.paola.todo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Método para buscar usuario por email
    Optional<Usuario> findByEmail(String email);

    //Metodo para revisar si existe el mail o el nock name
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

}
