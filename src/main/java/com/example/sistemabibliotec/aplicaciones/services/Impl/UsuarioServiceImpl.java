package com.example.sistemabibliotec.aplicaciones.services.Impl;

import com.example.sistemabibliotec.aplicaciones.services.UsuarioService;
import com.example.sistemabibliotec.domain.exception.BusinessException;
import com.example.sistemabibliotec.domain.exception.NotFoundException;
import com.example.sistemabibliotec.domain.model.EstadoUsuario;
import com.example.sistemabibliotec.domain.model.Usuario;
import com.example.sistemabibliotec.infraestructura.repository.UsuarioRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioServiceImpl(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public Usuario save(Usuario usuario) {
        if (usuario == null) {
            throw new BusinessException("Los datos del usuario son obligatorios");
        }
        validarDatos(usuario);

        // El documento no puede existir previamente
        Usuario usuarioEncontrado = usuarioRepositorio.findByDocumento(usuario.getDocumento()).orElse(null);

        if (usuarioEncontrado != null) {
            throw new BusinessException("El usuario con documento " + usuario.getDocumento() + " ya existe");
        }

        // Todo usuario nuevo se registra como HABILITADO
        usuario.setEstado(EstadoUsuario.HABILITADO);
        return usuarioRepositorio.save(usuario);
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepositorio.findAll();
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        return usuarioRepositorio.findById(id);
    }

    @Override
    public void delete(Integer id) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario con ID " + id + " no encontrado"));
        usuarioRepositorio.delete(usuario);
    }

    @Override
    public void update(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
            throw new BusinessException("El ID del usuario es obligatorio para actualizar");
        }
        validarDatos(usuario);

        Usuario existente = usuarioRepositorio.findById(usuario.getId())
                .orElseThrow(() -> new NotFoundException("Usuario con ID " + usuario.getId() + " no encontrado"));

        // El nuevo documento no puede pertenecer a otro usuario
        Usuario otroConDocumento = usuarioRepositorio.findByDocumento(usuario.getDocumento()).orElse(null);
        if (otroConDocumento != null && !otroConDocumento.getId().equals(existente.getId())) {
            throw new BusinessException("El documento " + usuario.getDocumento() + " ya pertenece a otro usuario");
        }

        existente.setNombre(usuario.getNombre());
        existente.setDocumento(usuario.getDocumento());
        if (usuario.getEstado() != null) {
            existente.setEstado(usuario.getEstado());
        }
        usuarioRepositorio.save(existente);
    }

    private void validarDatos(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new BusinessException("El nombre del usuario es obligatorio");
        }
        if (usuario.getDocumento() == null || usuario.getDocumento().isBlank()) {
            throw new BusinessException("El documento del usuario es obligatorio");
        }
    }
}

