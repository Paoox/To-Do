package com.paola.todo.repository;

import com.paola.todo.model.Publicacion;
import com.paola.todo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    // 🔍 Obtener publicaciones por usuario
    List<Publicacion> findByUsuario(Usuario usuario);

    // 🔢 Contar publicaciones por usuario
    long countByUsuario(Usuario usuario);
}

