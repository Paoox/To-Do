package com.paola.todo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private String url;
    private String tipo;
    private LocalDateTime fechaSubida;

    // Más adelante agregaremos esta relación:
    // @OneToOne(mappedBy = "avatar")
    // private Usuario usuario;

    public Avatar() {}

    public Avatar(String filename, String url, String tipo) {
        this.filename = filename;
        this.url = url;
        this.tipo = tipo;
        this.fechaSubida = LocalDateTime.now();
    }

    // Getters y setters

    public Long getId() { return id; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDateTime getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(LocalDateTime fechaSubida) { this.fechaSubida = fechaSubida; }
}
