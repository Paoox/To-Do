package com.paola.todo.controller;
import com.paola.todo.model.Usuario;
import com.paola.todo.repository.UsuarioRepository;
import com.paola.todo.dto.LoginRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import io.jsonwebtoken.security.Keys;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Obtener todos los usuarios
    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    // Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //creando nuevo usuario
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("email duplicado");
            }

            if (usuarioRepository.existsByNickname(usuario.getNickname())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("nickname duplicado");
            }

            // 🔐 Encriptar la contraseña
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

            // 📆 Asignar fecha actual si no se envió
            if (usuario.getFechaRegistro() == null) {
                usuario.setFechaRegistro(LocalDateTime.now());
            }

            // 👀 Asignar visualizaciones aleatorias si están en null o 0
            if (usuario.getVisualizaciones() == null || usuario.getVisualizaciones() == 0) {
                usuario.setVisualizaciones(ThreadLocalRandom.current().nextInt(1, 100));
            }

            // 🧑‍🎨 Asignar avatar aleatorio si no lo enviaron
            if (usuario.getAvatarUrl() == null || usuario.getAvatarUrl().isBlank()) {
                usuario.setAvatarUrl("https://api.dicebear.com/6.x/thumbs/svg?seed=" + UUID.randomUUID());
            }

            Usuario nuevo = usuarioRepository.save(usuario);
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error al crear usuario: " + e.getMessage());
        }
    }






    // Actualizar un usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario datosActualizados) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNombre(datosActualizados.getNombre());
                    usuario.setNickname(datosActualizados.getNickname());
                    usuario.setTelefono(datosActualizados.getTelefono());
                    usuario.setAvatarUrl(datosActualizados.getAvatarUrl());
                    usuario.setDescripcion(datosActualizados.getDescripcion());

                    // Solo encripta y actualiza la contraseña si viene en la petición
                    if (datosActualizados.getPassword() != null && !datosActualizados.getPassword().isEmpty()) {
                        usuario.setPassword(passwordEncoder.encode(datosActualizados.getPassword()));
                    }

                    return ResponseEntity.ok(usuarioRepository.save(usuario));
                })
                .orElse(ResponseEntity.notFound().build());
    }



    // Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Login de usuario
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String email = request.getEmail().trim();
        String password = request.getPassword().trim();

        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(email);
        if (!optionalUsuario.isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        Usuario usuario = optionalUsuario.get();
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Contraseña incorrecta");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        String token = Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("nombre", usuario.getNombre())
                .claim("id", usuario.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 día
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        Map<String, Object> usuarioSeguro = new HashMap<>();
        usuarioSeguro.put("id", usuario.getId());
        usuarioSeguro.put("nombre", usuario.getNombre());
        usuarioSeguro.put("email", usuario.getEmail());
        usuarioSeguro.put("nickname", usuario.getNickname());
        usuarioSeguro.put("telefono", usuario.getTelefono());
        usuarioSeguro.put("avatarUrl", usuario.getAvatarUrl());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        usuarioSeguro.put(
                "fechaRegistro",
                usuario.getFechaRegistro() != null ? usuario.getFechaRegistro().format(formatter) : ""
        );


        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("usuario", usuarioSeguro);

        return ResponseEntity.ok(response);
    }

    // ✅ DTO para resetear contraseña (solo email y nueva contraseña)
    public static class ResetPasswordRequest {
        public String email;
        public String newPassword;
    }

    // 🔐 Endpoint para restablecer la contraseña
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        String email = request.email.trim();
        String nuevaPassword = request.newPassword.trim();

        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(email);
        if (!optionalUsuario.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario con ese correo no encontrado.");
        }

        Usuario usuario = optionalUsuario.get();
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);

        System.out.println("🔁 Contraseña restablecida para: " + email);

        return ResponseEntity.ok("Contraseña actualizada correctamente.");
    }

    // 📩 Verifica si existe un correo (opcional)
    @GetMapping("/email/{email}")
    public ResponseEntity<?> verificarCorreo(@PathVariable String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email.trim());
        if (usuario.isPresent()) {
            return ResponseEntity.ok("Correo válido");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Correo no encontrado");
        }
    }
}
