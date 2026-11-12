package com.example.sistemabibliotec.aplicaciones.services;

import com.example.sistemabibliotec.domain.model.Libro;

import java.util.List;
import java.util.Optional;

public interface LibroService {

    Libro save(Libro libro);

    List<Libro> findAll();

    Optional<Libro> findById(Integer id);

    void delete(Integer id);

    void update(Libro libro);
}