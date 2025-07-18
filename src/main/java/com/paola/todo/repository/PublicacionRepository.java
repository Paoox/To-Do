package com.paola.todo.repository;

import com.paola.todo.model.Publicacion;
import com.paola.todo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    // 🔍 Obtener publicaciones por usuario (usando la entidad Usuario)
    List<Publicacion> findByUsuario(Usuario usuario);

    // 🔢 Contar publicaciones por usuario
    long countByUsuario(Usuario usuario);

    // ✅ Nuevo método: obtener publicaciones por ID del usuario (solo su ID)
    List<Publicacion> findByUsuarioId(Long usuarioId);
}
