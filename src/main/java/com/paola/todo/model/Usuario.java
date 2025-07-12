package com.paola.todo.model;

import jakarta.persistence.*;
import lombok.Data;

//le dice a spring que esta clase sera una tabla en la bd
@Entity
//nombre de la tabla
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    //genera en automatico el id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
}

