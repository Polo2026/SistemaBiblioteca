package com.example.sistemabibliotec.infraestructura.repository;

import com.example.sistemabibliotec.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByDocumento(String documento);
}