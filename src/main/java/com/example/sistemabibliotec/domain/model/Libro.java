package com.example.sistemabibliotec.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(nullable = false, unique = true, length = 13)
        private String isbn;

        @Column(nullable = false, length = 100)
        private String titulo;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 20)
        private EstadoLibro estado;

        public Libro(){
        }

    public Libro(Integer id, String isbn, String titulo, EstadoLibro estado) {
        this.id = id;
        this.isbn = isbn;
        this.titulo = titulo;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public EstadoLibro getEstado() {
        return estado;
    }

    public void setEstado(EstadoLibro estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", titulo='" + titulo + '\'' +
                ", estado=" + estado +
                '}';
    }
}
