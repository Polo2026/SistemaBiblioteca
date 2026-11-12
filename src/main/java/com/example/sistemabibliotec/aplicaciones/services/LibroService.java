package com.example.sistemabibliotec.aplicaciones.services;

import com.example.sistemabibliotec.domain.model.Libro;
import com.example.sistemabibliotec.domain.model.Usuario;
import org.hibernate.internal.util.Optional;

import java.util.List;

public interface LibroService {

    Libro save (Libro libro);
    List<Libro> findAll();
    Optional<Libro> findById(Integer id);
    void delete (Integer id);
    void update (Libro);

}