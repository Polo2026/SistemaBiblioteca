package com.example.sistemabibliotec.infraestructura.repository;

import com.example.sistemabibliotec.domain.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrestamoRepositorio extends JpaRepository<Prestamo, Integer> {


    List<Prestamo> findByUsuarioIdAndFechaDevolucionRealIsNull(Integer usuarioId);


    Optional<Prestamo> findByUsuarioIdAndLibroIdAndFechaDevolucionRealIsNull(Integer usuarioId, Integer libroId);
}