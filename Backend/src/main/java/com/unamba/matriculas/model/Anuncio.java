package com.unamba.matriculas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "anuncios")
public class Anuncio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_anuncio")
    private Long idAnuncio;
    
    @NotBlank(message = "El título es obligatorio")
    @Column(name = "titulo", nullable = false)
    private String titulo;
    
    @NotBlank(message = "El contenido es obligatorio")
    @Column(name = "contenido", nullable = false, columnDefinition = "TEXT")
    private String contenido;
    
    @NotNull(message = "El tipo es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoAnuncio tipo;
    
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;
    
    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    public enum TipoAnuncio {
        MATRICULA, EXAMEN, EVENTO, COMUNICADO, HORARIO
    }

    public Anuncio() {}

    public Anuncio(Long idAnuncio, String titulo, String contenido, TipoAnuncio tipo, LocalDateTime fechaInicio, LocalDateTime fechaFin, Boolean activo, LocalDateTime fechaCreacion) {
        this.idAnuncio = idAnuncio;
        this.titulo = titulo;
        this.contenido = contenido;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getIdAnuncio() { return idAnuncio; }
    public void setIdAnuncio(Long idAnuncio) { this.idAnuncio = idAnuncio; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public TipoAnuncio getTipo() { return tipo; }
    public void setTipo(TipoAnuncio tipo) { this.tipo = tipo; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
