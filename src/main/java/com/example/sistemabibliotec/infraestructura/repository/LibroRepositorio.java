package com.example.sistemabibliotec.infraestructura.repository;

import com.example.sistemabibliotec.domain.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibroRepositorio extends JpaRepository<Libro, Integer> {

    Optional<Libro> findByIsbn(String isbn);
}