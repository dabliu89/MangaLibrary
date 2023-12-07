package com.example.mangalibrary.Models;
import java.io.Serializable;
import java.time.LocalDate;

public class Manga implements Serializable {

    public String isbn;
    public String titulo;
    public String paginas;
    public String editora;
    public String dataPublicacao;
    public String capa;

    public Manga () {

    }

    public Manga (String isbn, String titulo, String paginas, String editora, String dataPublicacao, String capa) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.paginas = paginas;
        this.editora = editora;
        this.dataPublicacao = dataPublicacao;
        this.capa = capa;
    }

    public Manga (String titulo, String paginas, String editora, String dataPublicacao, String capa) {
        this.titulo = titulo;
        this.paginas = paginas;
        this.editora = editora;
        this.dataPublicacao = dataPublicacao;
        this.capa = capa;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPaginas() {
        return paginas;
    }

    public void setPaginas(String paginas) {
        this.paginas = paginas;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(String dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public String getCapa() {
        return capa;
    }

    public void setCapa(String capa) {
        this.capa = capa;
    }

    public String toString () {
        return this.titulo;
    }
}