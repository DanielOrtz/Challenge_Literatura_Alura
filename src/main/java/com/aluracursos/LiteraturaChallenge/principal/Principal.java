package com.aluracursos.LiteraturaChallenge.principal;

import com.aluracursos.LiteraturaChallenge.Datos.DatosResultados;
import com.aluracursos.LiteraturaChallenge.Datos.DatosAutor;
import com.aluracursos.LiteraturaChallenge.Datos.DatosLibros;
import com.aluracursos.LiteraturaChallenge.model.*;
import com.aluracursos.LiteraturaChallenge.repository.AutorRepositorio;
import com.aluracursos.LiteraturaChallenge.repository.LibroRepositorio;
import com.aluracursos.LiteraturaChallenge.service.ConsumoAPI;
import com.aluracursos.LiteraturaChallenge.service.ConvierteDatos;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private List<Autor> autores;
    private List<Libro> libros;
    private LibroRepositorio libroRepositorio;
    private AutorRepositorio autorRepositorio;

    public Principal(AutorRepositorio autorRepositorio, LibroRepositorio libroRepositorio) {
        this.autorRepositorio = autorRepositorio;
        this.libroRepositorio = libroRepositorio;
    }

    public void muestraElMenu() {

        System.out.println("""
                *****************************************************************************
                -------------------------------BIENVENIDOS-----------------------------------
                A continuacion se te presentara el menu de mi biblioteca.
                
                        1- Buscar libro por título
                        2- Lista de libros registrados
                        3- Lista de autores registrados
                        4- Listado de autores vivos en un determinado año
                        5- Listado de libros por idioma
                        6. Generar estadisticas
                        0- Salir                       
                -----------------------------------------------------------------------------
                Escribe la opción que deseas realizar:
                               
                 """);
    }

    public void ejecutarBuscador() {
        int opcion;
        do {
            muestraElMenu();
            if (teclado.hasNextInt()) {
                opcion = teclado.nextInt();
                teclado.nextLine();
                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;

                    case 2:
                        ListaDeLibrosRegistrados();
                        break;

                    case 3:
                        ListaAutoresRegistrados();
                        break;

                    case 4:
                        listaDeAutoresVivosEnDeterminadoAno();
                        break;

                    case 5:
                        listaDeLibrosPorIdioma();
                        break;
                    case 6:
                        generarEstadisticas();
                        break;
                    /*case 7:
                        top10LiborsDescargados();
                        break;*/
                    case 0:
                        System.out.println("Saliendo de la aplicacion, espero nos visites de nuevo");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } else {
                System.out.println("Opción no válida. Ingrese los datos correctos");
                teclado.nextLine();
                opcion = 0;
            }
        } while (opcion != 0);
    }

    private DatosResultados buscarDatosLibros() {
        System.out.println("Ingresa el titulo del libro que deseas encontrar");
        var libro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + libro.replace(" ", "+"));
        System.out.println(json);
        DatosResultados datosResultados = conversor.obtenerDatos(json, DatosResultados.class);
        return datosResultados;

    }

    private Libro crearLibro(DatosLibros datosLibros, Autor autor) {
        Libro libro = new Libro(datosLibros, autor);
        return libroRepositorio.save(libro);

    }


    private void buscarLibroPorTitulo() {
        DatosResultados datosResultados = buscarDatosLibros();

        if (!datosResultados.resultados().isEmpty()) {
            DatosLibros datosLibros = datosResultados.resultados().get(0);
            DatosAutor datosAutor = datosLibros.autor().get(0);
            Libro libroBusqueda = libroRepositorio.findByTituloIgnoreCase(datosLibros.titulo());
            System.out.println("\n" + "-------------Libro Encontrado-------------" +" \n");
            if (libroBusqueda != null) {
                System.out.println(libroBusqueda + "No se encuentra en la biblioteca, lo siento :(");

            } else {
                Autor autorBusqueda = autorRepositorio.findByNombreIgnoreCase(datosAutor.nombre());
                if (autorBusqueda == null) {
                    Autor autor = new Autor(datosAutor);
                    autorRepositorio.save(autor);
                    Libro libro = crearLibro(datosLibros, autor);
                    System.out.println(libro);

                } else {
                    Libro libro = crearLibro(datosLibros, autorBusqueda);
                    System.out.println(libro);
                    System.out.println("\n" + "-------------Libro No Encontrado-------------" +" \n");
                }
            }
        }
    }

    private void ListaDeLibrosRegistrados() {
        libros = libroRepositorio.findAll();

        libros.stream()
                .forEach(System.out::println);
    }

    private void ListaAutoresRegistrados() {
        autores = autorRepositorio.findAll();

        autores.stream()
                .forEach(System.out::println);

    }

    private void listaDeAutoresVivosEnDeterminadoAno() {
        System.out.println("Coloque a partir de que año desea buscar autores vivos");
        String fecha = teclado.nextLine();
        try {
            List<Autor> autoresVivos = autorRepositorio.autorVivoEnDeterminadoAno(fecha);

            autoresVivos.stream()
                    .forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("No hay autores vivos a partir del año que proporcionaste");
            throw new RuntimeException(e);
        }

    }

    private void listaDeLibrosPorIdioma() {
        System.out.println("""
                                
                Lista de libros por idioma
                            
                    1. Español (ES)
                    2. Ingles (EN)
                    3. Frances (FR)
                    4. Portugues (PT)
                    5. Italiano (IT)           
                    0. Regresar al menú principal
                    
                Ingrese el idioma para buscar los libros:
                                
                """);

        int opcion;

        if (teclado.hasNextInt()) {
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {

                case 1:
                    List<Libro> libroPorIdioma = libroRepositorio.findByIdiomasContaining("es");
                    libroPorIdioma.stream()
                            .forEach(System.out::println);
                    break;
                case 2:
                    libros = libroRepositorio.findByIdiomasContaining("en");
                    libros.stream()
                            .forEach(System.out::println);
                    break;
                case 3:
                    libros = libroRepositorio.findByIdiomasContaining("fr");
                    libros.stream()
                            .forEach(System.out::println);
                    break;

                case 4:
                    libros = libroRepositorio.findByIdiomasContaining("pt");
                    libros.stream()
                            .forEach(System.out::println);
                    break;
                case 5:
                    libros = libroRepositorio.findByIdiomasContaining("it");
                    libros.stream()
                            .forEach(System.out::println);
                    break;
                case 0:
                    System.out.println("Gracias por su preferencia");
                    ejecutarBuscador();
                    break;

                default:
                    System.out.println("Opción no válida. Ingrese el numero que se le presenta en el menu");

            }

        } else {
            System.out.println("Opción no válida. Ingrese el numero que se le presenta en el menu");
            teclado.nextLine();
            opcion = 0;
        }
    }
   /* private void top10LiborsDescargados() {
        System.out.println("\n Top 5 libros más descargados:\n");
        libros = LibroRepositorio.findTop10Descargados();
        libros.forEach(System.out::println);*/


    public void generarEstadisticas () {
        System.out.println("""
               GENERANDO ESTADÍSTICAS.... 
                     """);
        var json = consumoAPI.obtenerDatos(URL_BASE);
        var datos = conversor.obtenerDatos(json, DatosResultados.class);
        IntSummaryStatistics estadisticas = datos.resultados().stream()
                .filter(datosLibros -> datosLibros.descargas()>0)
                .collect(Collectors.summarizingInt(DatosLibros::descargas));

        Integer media = (int) estadisticas.getAverage();

        System.out.println("Media de descargas: " + media);
        System.out.println("Máxima de descargas: " + estadisticas.getMax());
        System.out.println("Mínima de descargas: " + estadisticas.getMin());
        System.out.println("Suma del numero de descargas: " + estadisticas.getSum());
        System.out.println("Total registros para calcular las estadísticas: " + estadisticas.getCount());
    }
    //Generar estadisticas metodo 2
     /*   var libros = libroRepositorio.findAll();
        DoubleSummaryStatistics doubleSummaryStatistics = new DoubleSummaryStatistics();
        for (Libro libro : libros) doubleSummaryStatistics.accept(libro.getDescargas());
        System.out.println("Conteo del numero de descargas - " + doubleSummaryStatistics.getCount());
        System.out.println("Numero de descargas minimo - " + doubleSummaryStatistics.getMin());
        System.out.println("Numero de descargas maximo - " + doubleSummaryStatistics.getMax());
        System.out.println("Suma del numero de descargas - " + doubleSummaryStatistics.getSum());
        System.out.println("Promedio del numero de descargas - " + doubleSummaryStatistics.getAverage() + "\n");
    }*/
}
