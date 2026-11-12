package com.example.sistemabibliotec.aplicaciones.services;

import com.example.sistemabibliotec.domain.model.Prestamo;

import java.util.List;
import java.util.Optional;

public interface PrestamoService {

        List<Prestamo> findAll();

        Optional<Prestamo> findById(Integer id);

        List<Prestamo> findActivosByUsuario(Integer usuarioId);

        Prestamo prestar(Integer usuarioId, Integer libroId);

        Prestamo devolver(Integer usuarioId, Integer libroId);
}


