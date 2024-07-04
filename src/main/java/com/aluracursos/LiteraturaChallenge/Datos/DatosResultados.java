package com.aluracursos.LiteraturaChallenge.Datos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosResultados(
        @JsonAlias("results") List<DatosLibros> resultados) {
}
