package com.paola.todo.controller;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import com.paola.todo.model.Publicacion;
import com.paola.todo.model.Usuario;
import com.paola.todo.repository.PublicacionRepository;
import com.paola.todo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


@RestController
@RequestMapping("/publicaciones")
@CrossOrigin(origins = "*") // ajusta si necesitas seguridad
public class PublicacionController {

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/crear")
    public ResponseEntity<?> crearPublicacionConImagen(
            @RequestParam("contenido") String contenido,
            @RequestParam("usuarioId") Long usuarioId,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen) {
        try {
            // 🔍 1. Buscar usuario por ID
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Usuario no encontrado");
            }

            // 🖼️ 2. Procesar imagen si viene incluida
            String imagenUrl = null;
            if (imagen != null && !imagen.isEmpty()) {
                String extension = StringUtils.getFilenameExtension(imagen.getOriginalFilename());
                String nombreArchivo = "post_" + UUID.randomUUID() + "." + extension;

                // 📁 Ruta donde guardar la imagen (usa 'uploads' montado desde Docker)
                Path uploadPath = Paths.get("uploads");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // 💾 Guardar la imagen físicamente
                Path archivoPath = uploadPath.resolve(nombreArchivo);
                Files.copy(imagen.getInputStream(), archivoPath, StandardCopyOption.REPLACE_EXISTING);

                // 🌐 Construir URL pública para acceder a la imagen
                imagenUrl = "/uploads/" + nombreArchivo;
            }

            // 📝 3. Crear la publicación
            Publicacion publicacion = new Publicacion();
            publicacion.setContenido(contenido);
            publicacion.setUsuario(usuarioOpt.get());
            publicacion.setFechaCreacion(LocalDateTime.now());
            publicacion.setImagenUrl(imagenUrl);
            publicacion.setLikes(0);
            publicacion.setReacciones(0);

            // 💾 4. Guardar en base de datos y devolver
            return ResponseEntity.ok(publicacionRepository.save(publicacion));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al subir la imagen");
        }
    }

    //Obtener publicaciones por id de usuario
    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> obtenerPublicacionesPorUsuario(@PathVariable Long id) {
        // 🔍 Buscar si existe el usuario
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }

        // 📄 Obtener las publicaciones desde el repositorio
        return ResponseEntity.ok(publicacionRepository.findByUsuarioId(id));
    }

    //Eliminar publicacion de usuario
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarPublicacion(@PathVariable Long id) {
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(id);
        if (publicacionOpt.isPresent()) {
            publicacionRepository.deleteById(id);
            return ResponseEntity.ok().body("Publicación eliminada");
        } else {
            return ResponseEntity.status(404).body("Publicación no encontrada");
        }
    }


}
