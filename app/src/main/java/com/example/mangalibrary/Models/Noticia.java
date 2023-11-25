package com.example.mangalibrary.Models;

import java.io.Serializable;

public class Noticia implements Serializable {

    public String titulo;
    public String imagem;
    public String texto;

    public Noticia (String titulo, String imagem, String texto) {
        this.titulo = titulo;
        this.imagem = imagem;
        this.texto = texto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return titulo;
    }

}