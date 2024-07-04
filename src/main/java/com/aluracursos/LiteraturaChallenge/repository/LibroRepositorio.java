package com.aluracursos.LiteraturaChallenge.repository;

import com.aluracursos.LiteraturaChallenge.model.Libro;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, Long> {
    Libro findByTituloIgnoreCase(String titulo);

    List<Libro> findByIdiomasContaining(String idioma);

    /*@Query("SELECT l FROM Libro l ORDER BY l.totalDeDescargas DESC LIMIT 10")
    List<Libro> findTop10Descargados();*/

}
