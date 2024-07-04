package com.aluracursos.LiteraturaChallenge.repository;

import com.aluracursos.LiteraturaChallenge.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepositorio extends JpaRepository<Autor, Long> {

    Autor findByNombreIgnoreCase(String nombre);

    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento >= :fecha")
    List<Autor> autorVivoEnDeterminadoAno(@Param("fecha") String fecha);
}
