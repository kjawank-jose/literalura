package com.kjawankjose.literalura.principal;

import com.kjawankjose.literalura.model.*;
import com.kjawankjose.literalura.repository.AutorRepository;
import com.kjawankjose.literalura.repository.LibroRepository;
import com.kjawankjose.literalura.service.ConsumoAPI;
import com.kjawankjose.literalura.service.ConvierteDatos;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository repositoryLibro;
    private AutorRepository repositoryAutor;
    private List<Autor> autores;
    private List<Libro> libros;

    public Principal(LibroRepository repositoryLibro, AutorRepository repositoryAutor) {
        this.repositoryLibro = repositoryLibro;
        this.repositoryAutor = repositoryAutor;
    }
    public void menuApp(){
        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    1. Buscar y guardar libro por título
                    2. Ver libros registrados
                    3. Ver autores registrados
                    4. Autores por año
                    5. Buscar libros por idioma
                    6. Los 10 libros más descargados
                    7. Libro con mayor y menor descargas 
                    
                    0. Salir
                    """;
            System.out.println(menu);
            while (!teclado.hasNextInt()){
                System.out.println("Ingrese una opción valida");
                teclado.nextLine();
            }
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion){
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    mostrarLibros();
                    break;
                case 3:
                    mostrarAutores();
                    break;
                case 4:
                    autoresPorAño();
                    break;
                case 5:
                    buscarLibrosPorIdioma();
                    break;
                case 6:
                    top10LibrosMasDescargados();
                    break;
                case 7:
                    librosMasDescargados();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación");
                    break;
                default:
                    System.out.println("Opción invalida");
            }
        }
    }
    private DatosBusqueda getBusqueda(){
        System.out.println("--------------------------------");
        System.out.println("Escribe el nombre del libro");
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace("", "%20"));
        DatosBusqueda datos = conversor.obtenerDatos(json, DatosBusqueda.class);
        return datos;
    }

    private void buscarLibro() {
        DatosBusqueda datosBusqueda = getBusqueda();
        if (datosBusqueda != null && !datosBusqueda.resultado().isEmpty()){
            DatosLibros primerLibro = datosBusqueda.resultado().get(0);

            Libro libro = new Libro(primerLibro);
            System.out.println("---------Libro---------");
            System.out.println(libro);
            System.out.println("-----------------------");

            Optional<Libro> libroExiste = repositoryLibro.findByTitulo(libro.getTitulo());
            if (libroExiste.isPresent()){
                System.out.println(libro + " ya fue registrado");
            } else {
                if(!primerLibro.autor().isEmpty()){
                    DatosAutor autor = primerLibro.autor().get(0);
                    Autor autor1 = new Autor(autor);
                    Optional<Autor> autorOptional = repositoryAutor.findByNombre(autor1.getNombre());

                    if (autorOptional.isPresent()){
                        Autor autorExiste = autorOptional.get();
                        libro.setAutor(autorExiste);
                        repositoryLibro.save(libro);
                    }else {
                        Autor autorNuevo = repositoryAutor.save(autor1);
                        libro.setAutor(autorNuevo);
                        repositoryLibro.save(libro);
                    }
                    Integer numeroDescargas = libro.getNumeroDeDescargas() != null ? libro.getNumeroDeDescargas():0;
                    System.out.println("---------Libro---------");
                    System.out.printf("Titulo: %s%nAutor: %s%nIdioma: %s%nNumero de Descargas: %s%n",
                            libro.getTitulo(), autor1.getNombre(), libro.getLanguage(), libro.getNumeroDeDescargas());
                    System.out.println("-----------------------");
                } else {
                    System.out.println("Sin autor");
                }
            }
        } else {
            System.out.println("El libro no fue ubicado");
        }
    }
    private void mostrarLibros(){
        libros = repositoryLibro.findAll();
        libros.stream()
                .forEach(System.out::println);
    }
    private void mostrarAutores(){
        autores = repositoryAutor.findAll();
        autores.stream()
                .forEach(System.out::println);
    }
    private void autoresPorAño(){
        System.out.println("Ingrese el año que desea consultar: ");
        var año = teclado.nextInt();
        autores.stream()
                .forEach(System.out::println);
    }
    private List<Libro> datosBusquedaIdioma(String idioma){
        var dato = Idioma.fromString(idioma);
        System.out.println("Idioma: " + dato);

        List<Libro> libroByLanguage = repositoryLibro.findByLanguage(dato);
        return libroByLanguage;
    }
    private void buscarLibrosPorIdioma(){
        System.out.println("Seleccione el igioma que desea buscar: ");

        var opcion = -1;
        while (opcion != 0){
            var opcionesDeIdioma = """
                    1. en - Inglés
                    2. es - Español
                    3. fr - Frances
                    4. pt - Portugués
                    
                    0. Regresar
                    """;
            System.out.println(opcionesDeIdioma);
            while (!teclado.hasNextInt()){
                System.out.println("Ingrese la opción correcta");
                teclado.nextLine();
            }
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion){
                case 1:
                    List<Libro> librosEnIngles = datosBusquedaIdioma("[en]");
                    librosEnIngles.forEach(System.out::println);
                    break;
                case 2:
                    List<Libro> librosEnEspañol = datosBusquedaIdioma("[es]");
                    librosEnEspañol.forEach(System.out::println);
                    break;
                case 3:
                    List<Libro> librosEnFrances = datosBusquedaIdioma("[fr]");
                    librosEnFrances.forEach(System.out::println);
                    break;
                case 4:
                    List<Libro> librosEnPortugues = datosBusquedaIdioma("[pt]");
                    librosEnPortugues.forEach(System.out::println);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Seleccione el idioma de la lista");
            }
        }
    }
    private void top10LibrosMasDescargados(){
        List<Libro> topLibros = repositoryLibro.top10LibrosMasDescargados();
        topLibros.forEach(System.out::println);
    }

    private void librosMasDescargados(){
        libros = repositoryLibro.findAll();
        IntSummaryStatistics est = libros.stream()
                .filter(l -> l.getNumeroDeDescargas() > 0)
                .collect(Collectors.summarizingInt(Libro::getNumeroDeDescargas));
        Libro libroConMasDescargas = libros.stream()
                .filter(l->l.getNumeroDeDescargas()== est.getMax())
                .findFirst()
                .orElse(null);

        Libro libroConMenosDescargas = libros.stream()
                .filter(l->l.getNumeroDeDescargas() ==  est.getMin())
                .findFirst()
                .orElse(null);
        System.out.println(" ↓↓↓↓↓↓↓↓↓ Descargas ↓↓↓↓↓↓↓↓↓ ");
        System.out.printf("%nLibro con mayor descargas: %s%nNúmero de descargas: " +
                "%d%n%nLibro con menos descargas: %s%nNúmero de descargas: " +
                "%d%n%n" ,libroConMasDescargas.getTitulo(), est.getMax(),
                libroConMenosDescargas.getTitulo(), est.getMin());
        System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
    }
}
















