package com.kjawankjose.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record DatosBusqueda(
        @JsonAlias("count") Integer cuenta,
        @JsonAlias("results")List<DatosLibros> resultado
        ) {
}
