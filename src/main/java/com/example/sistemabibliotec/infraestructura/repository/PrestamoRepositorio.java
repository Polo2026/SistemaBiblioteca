package com.example.sistemabibliotec.infraestructura.repository;

import com.example.sistemabibliotec.domain.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepositorio extends JpaRepository<Prestamo, Integer> {
}

