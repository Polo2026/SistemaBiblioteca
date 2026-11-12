package com.example.sistemabibliotec.aplicaciones.services;

import com.example.sistemabibliotec.domain.model.Prestamo;
import com.example.sistemabibliotec.domain.model.Usuario;
import org.hibernate.internal.util.Optional;

import java.util.List;

public interface PrestamoService {
        Prestamo save (Prestamo prestamo);
        List<Prestamo> findAll();
        Optional<Prestamo> findById(Integer id);
        void delete (Integer id);
        void update (Prestamo);

    }





