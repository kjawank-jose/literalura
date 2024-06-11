package com.kjawankjose.literalura.model;

public enum Idioma {
    en("[en]", "Ingles"),
    es("[es]", "Español"),
    fr("[fr]", "Frances"),
    pt("[pt]", "Portugues");

    private String idiomaGutendex;
    private String idiomaEspanol;

    Idioma(String idiomaGutendex, String idiomaEspanol){
        this.idiomaGutendex = idiomaGutendex;
        this.idiomaEspanol = idiomaEspanol;
    }

    public static Idioma fromString(String text){
        for (Idioma idioma : Idioma.values()){
            if (idioma.idiomaGutendex.equalsIgnoreCase(text)){
                return idioma;
            }
        }
        throw new IllegalArgumentException("No se encontro el idioma: " + text);
    }

    public static Idioma fromEspañol(String text){
        for (Idioma idioma : Idioma.values()){
            if (idioma.idiomaEspanol.equalsIgnoreCase(text)){
                return idioma;
            }
        }
        throw new IllegalArgumentException("No se encontro el idioma: " + text);
    }

    public String getIdiomaGutendex(){
        return idiomaGutendex;
    }
    public String getIdiomaEspanol(){
        return idiomaEspanol;
    }
}
