package com.example.sistemabibliotec.aplicaciones.services;

import com.example.sistemabibliotec.domain.model.Usuario;
import org.hibernate.internal.util.Optional;

import java.util.List;

public interface UsuarioService {
    Usuario save ( Usuario usuario);
    List<Usuario> findAll();
    Optional<Usuario> findById(Integer id);
    void delete (Integer id);
    void update (Usuario);
}


