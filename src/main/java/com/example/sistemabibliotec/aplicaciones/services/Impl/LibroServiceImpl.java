package com.example.sistemabibliotec.aplicaciones.services.Impl;

import com.example.sistemabibliotec.aplicaciones.services.LibroService;
import com.example.sistemabibliotec.domain.exception.BusinessException;
import com.example.sistemabibliotec.domain.exception.NotFoundException;
import com.example.sistemabibliotec.domain.model.EstadoLibro;
import com.example.sistemabibliotec.domain.model.Libro;
import com.example.sistemabibliotec.infraestructura.repository.LibroRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroServiceImpl implements LibroService {

    private final LibroRepositorio libroRepositorio;

    public LibroServiceImpl(LibroRepositorio libroRepositorio) {
        this.libroRepositorio = libroRepositorio;
    }

    @Override
    public Libro save(Libro libro) {
        if (libro == null) {
            throw new BusinessException("Los datos del libro son obligatorios");
        }
        validarDatos(libro);

        // El ISBN es único
        Libro libroEncontrado = libroRepositorio.findByIsbn(libro.getIsbn()).orElse(null);

        if (libroEncontrado != null) {
            throw new BusinessException("El libro con ISBN " + libro.getIsbn() + " ya existe");
        }

        // Todo libro nuevo entra al catálogo como DISPONIBLE
        libro.setEstado(EstadoLibro.DISPONIBLE);
        return libroRepositorio.save(libro);
    }

    @Override
    public List<Libro> findAll() {
        return libroRepositorio.findAll();
    }

    @Override
    public Optional<Libro> findById(Integer id) {
        return libroRepositorio.findById(id);
    }

    @Override
    public void delete(Integer id) {
        Libro libro = libroRepositorio.findById(id)
                .orElseThrow(() -> new NotFoundException("Libro con ID " + id + " no encontrado"));

        if (libro.getEstado() == EstadoLibro.PRESTADO) {
            throw new BusinessException("No se puede eliminar el libro '" + libro.getTitulo() + "' porque está prestado");
        }
        libroRepositorio.delete(libro);
    }

    @Override
    public void update(Libro libro) {
        if (libro == null || libro.getId() == null) {
            throw new BusinessException("El ID del libro es obligatorio para actualizar");
        }
        validarDatos(libro);

        Libro existente = libroRepositorio.findById(libro.getId())
                .orElseThrow(() -> new NotFoundException("Libro con ID " + libro.getId() + " no encontrado"));

        // El nuevo ISBN no puede pertenecer a otro libro
        Libro otroConIsbn = libroRepositorio.findByIsbn(libro.getIsbn()).orElse(null);
        if (otroConIsbn != null && !otroConIsbn.getId().equals(existente.getId())) {
            throw new BusinessException("El ISBN " + libro.getIsbn() + " ya pertenece a otro libro");
        }

        // Solo se actualizan isbn y título; el estado solo cambia con préstamos y devoluciones
        existente.setIsbn(libro.getIsbn());
        existente.setTitulo(libro.getTitulo());
        libroRepositorio.save(existente);
    }

    private void validarDatos(Libro libro) {
        if (libro.getIsbn() == null || libro.getIsbn().isBlank()) {
            throw new BusinessException("El ISBN del libro es obligatorio");
        }
        if (libro.getTitulo() == null || libro.getTitulo().isBlank()) {
            throw new BusinessException("El título del libro es obligatorio");
        }
    }
}