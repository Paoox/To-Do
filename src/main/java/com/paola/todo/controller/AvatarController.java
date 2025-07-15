package com.paola.todo.controller;

import com.paola.todo.model.Avatar;
import com.paola.todo.model.Usuario;
import com.paola.todo.repository.AvatarRepository;
import com.paola.todo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class AvatarController {

    private final AvatarRepository avatarRepository;
    private final UsuarioRepository usuarioRepository;

    @Value("${upload.dir:uploads}") // 👈 Se puede configurar en application.properties
    private String uploadDir;

    public AvatarController(AvatarRepository avatarRepository, UsuarioRepository usuarioRepository) {
        this.avatarRepository = avatarRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/{id}/avatar")
    @Transactional // ✅ Para garantizar consistencia entre avatar y usuario
    public ResponseEntity<?> subirAvatar(@PathVariable Long id,
                                         @RequestParam("archivo") MultipartFile archivo) {
        try {
            if (archivo.isEmpty()) {
                return ResponseEntity.badRequest().body("El archivo está vacío");
            }

            // 1. Buscar usuario
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Usuario usuario = optionalUsuario.get();

            // 2. Crear nombre único para archivo
            String extension = StringUtils.getFilenameExtension(archivo.getOriginalFilename());
            String nombreArchivo = "avatar_" + UUID.randomUUID() + "." + extension;

            // 3. Crear carpeta si no existe
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 4. Guardar archivo físico
            Path archivoPath = uploadPath.resolve(nombreArchivo);
            Files.copy(archivo.getInputStream(), archivoPath, StandardCopyOption.REPLACE_EXISTING);

            // 5. Construir URL pública
            String url = "/uploads/" + nombreArchivo;

            // 6. Guardar en tabla avatar
            Avatar avatar = new Avatar(nombreArchivo, url, archivo.getContentType());
            avatarRepository.save(avatar);

            // 7. Asociar al usuario y actualizar avatarUrl (si lo usas)
            usuario.setAvatar(avatar);
            usuario.setAvatarUrl(url); // opcional
            usuarioRepository.save(usuario);

            return ResponseEntity.ok(avatar);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al subir archivo");
        }
    }
}
