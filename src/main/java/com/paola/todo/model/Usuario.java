package com.paola.todo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // 🔐 El password no se expone al hacer GET
    private String password;

    private String telefono;

    private int visualizaciones;

    private LocalDateTime fechaRegistro;

    @Column(unique = true, nullable = false)
    private String nickname;

    private String descripcion;

    // 🌐 Este campo es opcional si usas avatar.getUrl(), pero lo dejamos por compatibilidad
    private String avatarUrl;

    // 🔗 Relación con la tabla Avatar (avatar_id es la FK en usuarios)
    @OneToOne
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Publicacion> publicaciones;

}
