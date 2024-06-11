package com.kjawankjose.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    @ManyToOne
    private Autor autor;
    @Enumerated(EnumType.STRING)
    private Idioma language;
    private Integer numeroDeDescargas;

    public Libro(){
    }

    public Libro(DatosLibros datosLibros){
        this.titulo = datosLibros.titulo();
        this.language = Idioma.fromString(datosLibros.idiomas().toString().split(",")[0].trim());
        this.numeroDeDescargas = datosLibros.numero_descargas();
    }

    @Override
    public String toString(){
        String nombreAutor = (autor != null) ? autor.getNombre() : "Autor desconocido";
        return String.format("---------- Libro ----------%nTitulo:" +
                " %s%nAutor: %s%nIdioma: %s%nNumero de Descargar:" +
                " %d%n---------------------------%n",titulo,nombreAutor,language,numeroDeDescargas);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Idioma getLanguage() {
        return language;
    }

    public void setLanguage(Idioma language) {
        this.language = language;
    }

    public Integer getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Integer numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }
}
