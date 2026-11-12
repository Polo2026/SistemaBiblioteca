package com.example.sistemabibliotec.aplicaciones.services.Impl;

import com.example.sistemabibliotec.aplicaciones.services.PrestamoService;
import com.example.sistemabibliotec.domain.exception.BusinessException;
import com.example.sistemabibliotec.domain.exception.MoraException;
import com.example.sistemabibliotec.domain.exception.NotFoundException;
import com.example.sistemabibliotec.domain.model.EstadoLibro;
import com.example.sistemabibliotec.domain.model.EstadoUsuario;
import com.example.sistemabibliotec.domain.model.Libro;
import com.example.sistemabibliotec.domain.model.Prestamo;
import com.example.sistemabibliotec.domain.model.Usuario;
import com.example.sistemabibliotec.infraestructura.repository.LibroRepositorio;
import com.example.sistemabibliotec.infraestructura.repository.PrestamoRepositorio;
import com.example.sistemabibliotec.infraestructura.repository.UsuarioRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    private static final int DIAS_PRESTAMO = 2;

    private final PrestamoRepositorio prestamoRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final LibroRepositorio libroRepositorio;

    public PrestamoServiceImpl(PrestamoRepositorio prestamoRepositorio,
                               UsuarioRepositorio usuarioRepositorio,
                               LibroRepositorio libroRepositorio) {
        this.prestamoRepositorio = prestamoRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.libroRepositorio = libroRepositorio;
    }

    @Override
    public List<Prestamo> findAll() {
        return prestamoRepositorio.findAll();
    }

    @Override
    public Optional<Prestamo> findById(Integer id) {
        return prestamoRepositorio.findById(id);
    }

    @Override
    public List<Prestamo> findActivosByUsuario(Integer usuarioId) {
        if (!usuarioRepositorio.existsById(usuarioId)) {
            throw new NotFoundException("Usuario con ID " + usuarioId + " no encontrado");
        }
        return prestamoRepositorio.findByUsuarioIdAndFechaDevolucionRealIsNull(usuarioId);
    }

    // noRollbackFor: si lanzamos MoraException, el cambio del usuario a INHABILITADO
    // debe quedar guardado en la base de datos (sin esto Spring hace rollback).
    @Override
    @Transactional(noRollbackFor = MoraException.class)
    public Prestamo prestar(Integer usuarioId, Integer libroId) {

        // 1. Verificar que el usuario y el libro existan
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuario con ID " + usuarioId + " no encontrado"));
        Libro libro = libroRepositorio.findById(libroId)
                .orElseThrow(() -> new NotFoundException("Libro con ID " + libroId + " no encontrado"));

        // 2. Validación de mora: préstamos activos con fecha límite anterior a hoy
        LocalDate hoy = LocalDate.now();
        List<Prestamo> activos = prestamoRepositorio.findByUsuarioIdAndFechaDevolucionRealIsNull(usuarioId);
        boolean tieneVencidos = activos.stream()
                .anyMatch(p -> p.getFechaLimiteDevolucion().isBefore(hoy));

        if (tieneVencidos) {
            usuario.setEstado(EstadoUsuario.INHABILITADO);
            usuarioRepositorio.save(usuario);
            throw new MoraException("El usuario " + usuario.getNombre()
                    + " queda inhabilitado por tener préstamos vencidos");
        }

        // 3. El usuario debe estar HABILITADO
        if (usuario.getEstado() != EstadoUsuario.HABILITADO) {
            throw new BusinessException("El usuario " + usuario.getNombre() + " está inhabilitado");
        }

        // 4. El libro debe estar DISPONIBLE
        if (libro.getEstado() != EstadoLibro.DISPONIBLE) {
            throw new BusinessException("El libro '" + libro.getTitulo() + "' ya está prestado");
        }

        // 5. Registro automático de fechas y actualización del libro
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(hoy);
        prestamo.setFechaLimiteDevolucion(hoy.plusDays(DIAS_PRESTAMO));

        libro.setEstado(EstadoLibro.PRESTADO);
        libroRepositorio.save(libro);

        return prestamoRepositorio.save(prestamo);
    }

    @Override
    @Transactional
    public Prestamo devolver(Integer usuarioId, Integer libroId) {

        // Debe existir un préstamo activo de ese usuario para ese libro
        Prestamo prestamo = prestamoRepositorio
                .findByUsuarioIdAndLibroIdAndFechaDevolucionRealIsNull(usuarioId, libroId)
                .orElseThrow(() -> new BusinessException("No existe un préstamo activo del usuario "
                        + usuarioId + " para el libro " + libroId));

        prestamo.setFechaDevolucionReal(LocalDate.now());

        Libro libro = prestamo.getLibro();
        libro.setEstado(EstadoLibro.DISPONIBLE);
        libroRepositorio.save(libro);

        return prestamoRepositorio.save(prestamo);
    }
}