package com.paola.todo.controller;

import com.paola.todo.model.Publicacion;
import com.paola.todo.model.Usuario;
import com.paola.todo.repository.PublicacionRepository;
import com.paola.todo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/publicaciones")
@CrossOrigin(origins = "*") // ajusta si necesitas seguridad
public class PublicacionController {

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 🟢 Crear una nueva publicación
    @PostMapping
    public Publicacion crearPublicacion(@RequestBody Publicacion publicacion) {
        publicacion.setFechaCreacion(LocalDateTime.now());
        return publicacionRepository.save(publicacion);
    }

    // 🔵 Obtener todas las publicaciones
    @GetMapping
    public List<Publicacion> obtenerTodas() {
        return publicacionRepository.findAll();
    }

    // 🔵 Obtener publicaciones por id de usuario
    @GetMapping("/usuario/{id}")
    public List<Publicacion> publicacionesPorUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(publicacionRepository::findByUsuario).orElse(List.of());
    }

    // 🟡 Editar publicación (solo contenido e imagen por ahora)
    @PutMapping("/{id}")
    public Publicacion editarPublicacion(@PathVariable Long id, @RequestBody Publicacion nueva) {
        return publicacionRepository.findById(id).map(pub -> {
            pub.setContenido(nueva.getContenido());
            pub.setImagenUrl(nueva.getImagenUrl());
            return publicacionRepository.save(pub);
        }).orElse(null);
    }

    // 🔴 Eliminar publicación
    @DeleteMapping("/{id}")
    public void eliminarPublicacion(@PathVariable Long id) {
        publicacionRepository.deleteById(id);
    }

    // ❤️ Aumentar likes
    @PostMapping("/{id}/like")
    public Publicacion darLike(@PathVariable Long id) {
        return publicacionRepository.findById(id).map(pub -> {
            pub.setLikes(pub.getLikes() + 1);
            return publicacionRepository.save(pub);
        }).orElse(null);
    }

    // 🥳 Aumentar reacciones
    @PostMapping("/{id}/reaccionar")
    public Publicacion reaccionar(@PathVariable Long id) {
        return publicacionRepository.findById(id).map(pub -> {
            pub.setReacciones(pub.getReacciones() + 1);
            return publicacionRepository.save(pub);
        }).orElse(null);
    }
}

