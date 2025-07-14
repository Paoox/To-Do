package com.paola.todo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    // ⚠️ Solo ocultamos el password al convertir a JSON de salida, no de entrada
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    private String telefono;

    private int visualizaciones;

    private LocalDateTime fechaRegistro;

    private String avatarUrl;

    @Column(unique = true, nullable = false)
    private String nickname;
}
