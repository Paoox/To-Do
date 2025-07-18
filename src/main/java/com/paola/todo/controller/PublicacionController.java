package com.paola.todo.controller;

import com.paola.todo.model.Publicacion;
import com.paola.todo.model.Usuario;
import com.paola.todo.repository.PublicacionRepository;
import com.paola.todo.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/publicaciones")
@CrossOrigin(origins = "*") // ⚠️ Puedes restringir esto a tu dominio para mayor seguridad
public class PublicacionController {

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 📌 CREAR publicación con o sin imagen
    @PostMapping("/crear")
    public ResponseEntity<?> crearPublicacionConImagen(
            @RequestParam("contenido") String contenido,
            @RequestParam("usuarioId") Long usuarioId,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen) {
        try {
            // 🔎 Buscar el usuario
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Usuario no encontrado");
            }

            // 🖼️ Procesar imagen si se envía
            String imagenUrl = null;
            if (imagen != null && !imagen.isEmpty()) {
                String extension = StringUtils.getFilenameExtension(imagen.getOriginalFilename());
                String nombreArchivo = "post_" + UUID.randomUUID() + "." + extension;

                Path uploadPath = Paths.get("uploads");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Files.copy(imagen.getInputStream(), uploadPath.resolve(nombreArchivo), StandardCopyOption.REPLACE_EXISTING);
                imagenUrl = "/uploads/" + nombreArchivo;
            }

            // ✍️ Crear y guardar la publicación
            Publicacion publicacion = new Publicacion();
            publicacion.setContenido(contenido);
            publicacion.setUsuario(usuarioOpt.get());
            publicacion.setFechaCreacion(LocalDateTime.now());
            publicacion.setImagenUrl(imagenUrl);
            publicacion.setLikes(0);
            publicacion.setReacciones(0);

            return ResponseEntity.ok(publicacionRepository.save(publicacion));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al subir la imagen");
        }
    }

    // 📌 OBTENER publicaciones por ID de usuario
    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> obtenerPublicacionesPorUsuario(@PathVariable Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }

        return ResponseEntity.ok(publicacionRepository.findByUsuarioId(id));
    }

    // 📌 ELIMINAR publicación por ID
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarPublicacion(@PathVariable Long id) {
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(id);
        if (publicacionOpt.isPresent()) {
            publicacionRepository.deleteById(id);
            return ResponseEntity.ok("Publicación eliminada");
        } else {
            return ResponseEntity.status(404).body("Publicación no encontrada");
        }
    }

    // 📌 EDITAR publicación por ID (contenido + imagen)
    @PutMapping("/{id}") // 🛠️ Importante: NO repitas "/publicaciones", ya viene de @RequestMapping
    public ResponseEntity<?> actualizarPublicacion(
            @PathVariable Long id,
            @RequestParam("contenido") String contenido,
            @RequestParam(value = "imagen", required = false) MultipartFile nuevaImagen,
            @RequestParam("eliminarImagen") boolean eliminarImagen) {

        Optional<Publicacion> optional = publicacionRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publicación no encontrada");
        }

        Publicacion publicacion = optional.get();
        publicacion.setContenido(contenido);

        // 🗑️ Eliminar imagen anterior si se indicó
        if (eliminarImagen && publicacion.getImagenUrl() != null) {
            String nombreArchivo = Paths.get(publicacion.getImagenUrl()).getFileName().toString();
            File archivo = new File("uploads/" + nombreArchivo);
            if (archivo.exists()) archivo.delete();
            publicacion.setImagenUrl(null);
        }

        // 📥 Guardar nueva imagen si viene
        if (nuevaImagen != null && !nuevaImagen.isEmpty()) {
            try {
                String extension = StringUtils.getFilenameExtension(nuevaImagen.getOriginalFilename());
                String nuevoNombre = "post_" + UUID.randomUUID() + "." + extension;
                Path ruta = Paths.get("uploads").resolve(nuevoNombre);
                Files.copy(nuevaImagen.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);
                publicacion.setImagenUrl("/uploads/" + nuevoNombre);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir la imagen");
            }
        }

        return ResponseEntity.ok(publicacionRepository.save(publicacion));
    }

    // 📌 OBTENER todas las publicaciones del sistema
    @GetMapping
    public ResponseEntity<?> obtenerTodasLasPublicaciones() {
        return ResponseEntity.ok(publicacionRepository.findAll());
    }

    // 📌 Aumentar like en publicación
    @PutMapping("/{id}/like")
    public ResponseEntity<?> darLike(@PathVariable Long id) {
        Optional<Publicacion> optional = publicacionRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publicación no encontrada");
        }

        Publicacion publicacion = optional.get();
        publicacion.setLikes(publicacion.getLikes() + 1);
        return ResponseEntity.ok(publicacionRepository.save(publicacion));
    }

    // 📌 Aumentar reacción en publicación
    @PutMapping("/{id}/reaccion")
    public ResponseEntity<?> reaccionar(@PathVariable Long id) {
        Optional<Publicacion> optional = publicacionRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publicación no encontrada");
        }

        Publicacion publicacion = optional.get();
        publicacion.setReacciones(publicacion.getReacciones() + 1);
        return ResponseEntity.ok(publicacionRepository.save(publicacion));
    }



}
