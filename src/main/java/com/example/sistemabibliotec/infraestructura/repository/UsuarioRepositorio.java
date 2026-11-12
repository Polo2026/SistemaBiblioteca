package com.example.sistemabibliotec.infraestructura.repository;

import com.example.sistemabibliotec.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
}