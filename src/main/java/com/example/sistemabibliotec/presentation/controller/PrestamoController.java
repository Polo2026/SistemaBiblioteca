package com.example.sistemabibliotec.presentation.controller;

import com.example.sistemabibliotec.aplicaciones.services.PrestamoService;
import com.example.sistemabibliotec.domain.exception.BusinessException;
import com.example.sistemabibliotec.domain.exception.MoraException;
import com.example.sistemabibliotec.domain.exception.NotFoundException;
import com.example.sistemabibliotec.domain.model.Prestamo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            return ResponseEntity.ok(prestamoService.findAll());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + exception.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        try {
            Prestamo prestamo = prestamoService.findById(id)
                    .orElseThrow(() -> new NotFoundException("Préstamo con ID " + id + " no encontrado"));
            return ResponseEntity.ok(prestamo);
        } catch (NotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + exception.getMessage());
        }
    }

    // Consultar libros prestados por usuario (préstamos con fechaDevolucionReal nula)
    @GetMapping("/usuario/{usuarioId}/activos")
    public ResponseEntity<?> findActivosByUsuario(@PathVariable Integer usuarioId) {
        try {
            return ResponseEntity.ok(prestamoService.findActivosByUsuario(usuarioId));
        } catch (NotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + exception.getMessage());
        }
    }

    // Proceso de préstamo (7.3 del taller)
    @PostMapping("/prestar")
    public ResponseEntity<?> prestar(@RequestParam Integer usuarioId, @RequestParam Integer libroId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(prestamoService.prestar(usuarioId, libroId));
        } catch (NotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } catch (MoraException exception) {
            // MoraException va antes que BusinessException por si una hereda de la otra
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (BusinessException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + exception.getMessage());
        }
    }

    // Proceso de devolución (7.4 del taller)
    @PutMapping("/devolver")
    public ResponseEntity<?> devolver(@RequestParam Integer usuarioId, @RequestParam Integer libroId) {
        try {
            return ResponseEntity.ok(prestamoService.devolver(usuarioId, libroId));
        } catch (NotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } catch (MoraException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (BusinessException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + exception.getMessage());
        }
    }
}