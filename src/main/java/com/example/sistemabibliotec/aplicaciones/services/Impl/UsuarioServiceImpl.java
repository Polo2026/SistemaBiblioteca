package com.example.sistemabibliotec.aplicaciones.services.Impl;

import com.example.sistemabibliotec.Exception.UsuarioEncontradoException;
import com.example.sistemabibliotec.Exception.UsuarioInvalidoException;
import com.example.sistemabibliotec.aplicaciones.services.UsuarioService;
import com.example.sistemabibliotec.domain.model.Usuario;
import com.example.sistemabibliotec.infraestructura.repository.UsuarioRepositorio;
import org.hibernate.internal.util.Optional;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioServiceImpl(UsuarioRepositorio usuarioRepositorio){
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public Usuario save (Usuario usuario){
        if (usuario == null){
            throw new UsuarioInvalidoException("Los datos del usuario son Obligatorio");
        }
        Usuario usuarioEncontrado = usuarioRepositorio.findById(usuario.getId()).orElse(null);

        if (usuarioEncontrado != null){
            throw new UsuarioEncontradoException("El usuario con ID" + usuario.getId() +" ya existe");
        }
        return usuarioRepositorio.save(usuario);
    }

    @Override
    public List<Usuario> findAll() {
        return List.of();
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void update() {

    }


}


