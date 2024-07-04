package com.aluracursos.LiteraturaChallenge;

import com.aluracursos.LiteraturaChallenge.principal.Principal;
import com.aluracursos.LiteraturaChallenge.repository.AutorRepositorio;
import com.aluracursos.LiteraturaChallenge.repository.LibroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaChallengeApplication implements CommandLineRunner {

	@Autowired
	private LibroRepositorio libroRepositorio;

	@Autowired
	AutorRepositorio autorRepositorio;

	public static void main(String[] args) {

		SpringApplication.run(LiteraturaChallengeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal (autorRepositorio, libroRepositorio);
		principal.ejecutarBuscador();
	}
}

