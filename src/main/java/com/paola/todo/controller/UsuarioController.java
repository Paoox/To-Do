package com.paola.todo.controller;

import com.paola.todo.model.Usuario;
import com.paola.todo.repository.UsuarioRepository;
import com.paola.todo.dto.LoginRequest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.jsonwebtoken.security.Keys;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private static final byte[] JWT_SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
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

    // Crear nuevo usuario con contraseña encriptada
    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    // Actualizar un usuario
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
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET), SignatureAlgorithm.HS256)
                .compact();

        Map<String, Object> usuarioSeguro = new HashMap<>();
        usuarioSeguro.put("id", usuario.getId());
        usuarioSeguro.put("nombre", usuario.getNombre());
        usuarioSeguro.put("email", usuario.getEmail());
        usuarioSeguro.put("nickname", usuario.getNickname());
        usuarioSeguro.put("telefono", usuario.getTelefono());
        usuarioSeguro.put("avatarUrl", usuario.getAvatarUrl());
        usuarioSeguro.put("fechaRegistro", usuario.getFechaRegistro().toString());

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
