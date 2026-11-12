package com.example.sistemabibliotec.presentation.controller;

import com.example.sistemabibliotec.aplicaciones.services.LibroService;
import com.example.sistemabibliotec.domain.exception.BusinessException;
import com.example.sistemabibliotec.domain.exception.NotFoundException;
import com.example.sistemabibliotec.domain.model.Libro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            return ResponseEntity.ok(libroService.findAll());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + exception.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        try {
            Libro libro = libroService.findById(id)
                    .orElseThrow(() -> new NotFoundException("Libro con ID " + id + " no encontrado"));
            return ResponseEntity.ok(libro);
        } catch (NotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + exception.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Libro libro) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(libroService.save(libro));
        } catch (NotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } catch (BusinessException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + exception.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Libro libro) {
        try {
            libroService.update(libro);
            return ResponseEntity.ok("Libro actualizado correctamente");
        } catch (NotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } catch (BusinessException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + exception.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            libroService.delete(id);
            return ResponseEntity.ok("Libro eliminado correctamente");
        } catch (NotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } catch (BusinessException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + exception.getMessage());
        }
    }
}